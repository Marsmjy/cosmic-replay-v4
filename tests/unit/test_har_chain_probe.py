import json

from lib.har_chain_probe import build_experience_catalog, probe_har_chain


def _write_probe_har(path):
    har = {
        "log": {
            "entries": [
                {
                    "request": {
                        "method": "POST",
                        "url": "https://example.test/ierp/form/invokeAction.do?appId=hpdi&f=hpdi_choice&ac=getLookUpList",
                        "postData": {
                            "text": 'pageId=abcdefabcdefabcdefabcdefabcdefab&appId=hpdi&params=[{"key":"bizitemgroup","methodName":"getLookUpList","args":[["%","","%",0,1,0]],"postData":[]}]'
                        },
                    },
                    "response": {
                        "status": 200,
                        "content": {
                            "text": json.dumps(
                                [
                                    {
                                        "a": "InvokeControlMethod",
                                        "p": [
                                            {
                                                "key": "bizitemgroup",
                                                "methodname": "setLookUpListValue",
                                                "args": [
                                                    {
                                                        "k": "bizitemgroup",
                                                        "data": [["internal", "CODE", "Name"]],
                                                        "columns": [{"id": "id"}],
                                                    }
                                                ],
                                            }
                                        ],
                                    }
                                ],
                                ensure_ascii=False,
                            )
                        },
                    },
                },
                {
                    "request": {
                        "method": "POST",
                        "url": "https://example.test/ierp/form/batchInvokeAction.do?appId=hpdi&f=hpdi_choice&ac=setItemByIdFromClient",
                        "postData": {
                            "text": 'pageId=abcdefabcdefabcdefabcdefabcdefab&appId=hpdi&params=[{"key":"bizitemgroup","methodName":"setItemByIdFromClient","args":[["internal",0]],"postData":[{},[]]}]'
                        },
                    },
                    "response": {
                        "status": 200,
                        "content": {
                            "text": json.dumps(
                                [
                                    {"a": "u", "p": [{"k": "bizitemgroup", "v": ["CODE", "Name", "CODE", "", ""]}]},
                                    {
                                        "a": "showForm",
                                        "p": [
                                            {
                                                "formId": "outer_f7",
                                                "billFormId": "inner_real_list",
                                                "pageId": "11111111111111111111111111111111",
                                            }
                                        ],
                                    },
                                ],
                                ensure_ascii=False,
                            )
                        },
                    },
                },
                {
                    "request": {
                        "method": "POST",
                        "url": "https://example.test/ierp/form/batchInvokeAction.do?appId=hpdi&f=inner_real_list&ac=loadData",
                        "postData": {
                            "text": 'pageId=11111111111111111111111111111111&appId=hpdi&params=[{"key":"","methodName":"loadData","args":[],"postData":[]}]'
                        },
                    },
                    "response": {"status": 200, "content": {"text": "[]"}},
                },
                {
                    "request": {
                        "method": "POST",
                        "url": "https://example.test/ierp/form/batchInvokeAction.do?appId=hpdi&f=hpdi_bill&ac=save",
                        "postData": {
                            "text": 'pageId=22222222222222222222222222222222&appId=hpdi&params=[{"key":"tbmain","methodName":"itemClick","args":["bar_save","save"],"postData":[{},[]]}]'
                        },
                    },
                    "response": {"status": 200, "content": {"text": "[]"}},
                },
            ]
        }
    }
    path.write_text(json.dumps(har), encoding="utf-8")


def test_har_chain_probe_detects_lookup_alias_and_write_anchor(tmp_path):
    path = tmp_path / "demo.har"
    _write_probe_har(path)

    probe = probe_har_chain(path)

    assert probe["summary"]["lookup_prefetch_count"] == 1
    assert probe["summary"]["showform_alias_count"] == 1
    assert probe["summary"]["write_anchor_count"] == 1
    assert probe["summary"]["pageid_exact_link_count"] == 1
    assert probe["links"]["lookup_prefetches"][0]["prefetch_endpoint"] == "invokeAction.do"
    assert probe["links"]["showform_aliases"][0]["future_request_event"] == "e0003"
    assert probe["links"]["pageid_flows"][0]["producer_event"] == "e0002"
    assert probe["links"]["pageid_flows"][0]["consumer_event"] == "e0003"
    assert probe["links"]["pageid_flows"][0]["form_scope_match"] is True
    assert "lookup_prefetch_before_pick" in [item["code"] for item in probe["lessons"]]
    raw = json.dumps(probe, ensure_ascii=False)
    assert "internal" not in raw
    assert "CODE" not in raw
    assert "Name" not in raw
    assert "11111111111111111111111111111111" not in raw


def test_har_chain_experience_catalog_is_compact(tmp_path):
    path = tmp_path / "demo.har"
    _write_probe_har(path)
    probe = probe_har_chain(path)

    catalog = build_experience_catalog([
        {"id": "demo", "title": "Demo", "business_type": "unit", "probe": probe}
    ])

    assert catalog["sample_count"] == 1
    assert catalog["samples"][0]["lesson_codes"] == [
        "lookup_prefetch_before_pick",
        "showform_billformid_alias",
        "load_data_default_context",
        "write_anchor_present",
        "exact_pageid_producer_consumer_flow",
    ]


def test_har_chain_probe_flags_pageid_reuse_after_close(tmp_path):
    page_id = "3" * 32
    har = {
        "log": {
            "entries": [
                {
                    "request": {
                        "method": "POST",
                        "url": "https://example.test/ierp/form/invokeAction.do?appId=demo&f=list_form&ac=entryRowClick",
                        "postData": {
                            "text": 'pageId=root' + "a" * 32 + '&params=[{"methodName":"entryRowClick"}]'
                        },
                    },
                    "response": {
                        "status": 200,
                        "content": {
                            "text": json.dumps([{
                                "a": "showForm",
                                "p": [{"formId": "detail_form", "pageId": page_id}],
                            }])
                        },
                    },
                },
                {
                    "request": {
                        "method": "POST",
                        "url": "https://example.test/ierp/form/invokeAction.do?appId=demo&f=detail_form&ac=close",
                        "postData": {
                            "text": f'pageId={page_id}&params=[{{"methodName":"close"}}]'
                        },
                    },
                    "response": {
                        "status": 200,
                        "content": {
                            "text": json.dumps([{
                                "a": "closeWindow",
                                "p": [{"pageId": page_id}],
                            }])
                        },
                    },
                },
                {
                    "request": {
                        "method": "POST",
                        "url": "https://example.test/ierp/form/invokeAction.do?appId=demo&f=detail_form&ac=loadData",
                        "postData": {
                            "text": f'pageId={page_id}&params=[{{"methodName":"loadData"}}]'
                        },
                    },
                    "response": {"status": 200, "content": {"text": "[]"}},
                },
            ]
        }
    }
    path = tmp_path / "reuse.har"
    path.write_text(json.dumps(har), encoding="utf-8")

    probe = probe_har_chain(path)

    assert probe["summary"]["pageid_reuse_after_close_count"] == 1
    assert page_id not in json.dumps(probe)
