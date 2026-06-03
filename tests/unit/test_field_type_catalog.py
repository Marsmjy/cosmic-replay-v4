from pathlib import Path

from lib.field_type_catalog import (
    canonical_category,
    classify_for_import,
    get_field_entry,
    save_catalog,
    panel_for_category,
    update_catalog_from_resolver,
)
from lib.metadata_resolver import FieldMeta


class FakeMetadataResolver:
    def __init__(self):
        self.entities = {
            "hcdm_targetsalary": {
                "name": FieldMeta("name", "名称", "MuliLangTextProp", True),
                "employee": FieldMeta("employee", "定调薪人员", "BasedataProp", True, base_entity="bos_user"),
                "levels": FieldMeta("levels", "薪酬水平", "MulBasedataProp", False, base_entity="khr_salarylevel"),
                "salaryproposal": FieldMeta("salaryproposal", "是否薪酬提案", "ComboProp", False, combo_items={"1": "是", "0": "否"}),
                "enabled": FieldMeta("enabled", "启用", "BooleanProp", False),
                "effectivedate": FieldMeta("effectivedate", "生效日期", "DateProp", True),
                "entryentity": FieldMeta("entryentity", "定调薪明细", "EntryProp", False),
            }
        }

    def get_entity_fields(self, entity_id):
        return self.entities.get(entity_id)


def test_canonical_field_type_taxonomy():
    assert canonical_category("TextProp") == "text"
    assert canonical_category("MuliLangTextProp") == "multi_lang_text"
    assert canonical_category("BasedataProp") == "basedata"
    assert canonical_category("MulBasedataProp") == "multi_basedata"
    assert canonical_category("ComboProp") == "combo"
    assert canonical_category("MulComboProp") == "multi_combo"
    assert canonical_category("BooleanProp") == "boolean"
    assert canonical_category("DateProp") == "date"
    assert canonical_category("DateTimeProp") == "datetime"
    assert canonical_category("EntryProp") == "entry"
    assert panel_for_category("basedata") == "pick_fields"
    assert panel_for_category("multi_lang_text") == "vars"


def test_hr_runtime_field_type_taxonomy():
    assert canonical_category("kd.bos.ext.hr.entity.property.AdminOrgFieldProp") == "basedata"
    assert canonical_category("MainOrgProp") == "basedata"
    assert canonical_category("kd.bos.ext.hr.entity.property.PositionFieldProp") == "basedata"
    assert canonical_category("kd.bos.ext.hr.entity.property.HisModelBasedataProp") == "basedata"
    assert canonical_category("kd.bos.ext.hr.metadata.prop.QueryProp") == "basedata"
    assert canonical_category("kd.bos.ext.hr.metadata.prop.MulQueryProp") == "multi_basedata"
    assert canonical_category("kd.bos.ext.hr.entity.property.MulAdminOrgFieldProp") == "multi_basedata"
    assert canonical_category("DynamicLocaleProperty") == "multi_lang_text"
    assert canonical_category("TelephoneProp") == "text"
    assert canonical_category("TreeEntryProp") == "entry"
    assert canonical_category("kd.bos.entity.property.PictureProp") == "attachment"
    assert panel_for_category("attachment") == "structural"


def test_update_catalog_from_resolver_persists_field_types(tmp_path: Path):
    catalog_path = tmp_path / "runtime_field_type_catalog.json"

    status = update_catalog_from_resolver(
        FakeMetadataResolver(),
        {"hcdm_targetsalary"},
        path=catalog_path,
    )

    assert status["entity_count"] == 1
    assert status["field_count"] == 7
    employee = get_field_entry("hcdm_targetsalary", "employee", path=catalog_path)
    assert employee["raw_type"] == "BasedataProp"
    assert employee["category"] == "basedata"
    assert employee["panel"] == "pick_fields"
    assert employee["base_entity"] == "bos_user"
    proposal = get_field_entry("hcdm_targetsalary", "salaryproposal", path=catalog_path)
    assert proposal["combo_items"] == {"1": "是", "0": "否"}


def test_classify_for_import_uses_raw_type_before_heuristics():
    item = classify_for_import(
        form_id="hcdm_targetsalary",
        field_key="salaryproposal",
        raw_type="ComboProp",
        value="1",
    )

    assert item["category"] == "combo"
    assert item["panel"] == "pick_fields"
    assert item["source"] == "metadata"


def test_classify_for_import_reclassifies_stale_unknown_catalog_entry(tmp_path: Path):
    catalog_path = tmp_path / "runtime_field_type_catalog.json"
    save_catalog(
        {
            "schema_version": 1,
            "updated_at": "",
            "type_counts": {},
            "entities": {
                "haos_adminorgdetail": {
                    "fields": {
                        "belongadminorg": {
                            "raw_type": "kd.bos.ext.hr.entity.property.AdminOrgFieldProp",
                            "category": "unknown",
                            "panel": "unknown",
                            "label": "所属行政组织",
                        }
                    }
                }
            },
        },
        path=catalog_path,
    )

    item = classify_for_import(
        form_id="haos_adminorgdetail",
        field_key="belongadminorg",
        path=catalog_path,
    )

    assert item["category"] == "basedata"
    assert item["panel"] == "pick_fields"
