"""
cosmic-replay v2 - 测试数据工厂

提供可复用的测试数据生成方法
"""
import json
from pathlib import Path
from typing import Any


class TestDataFactory:
    """测试数据工厂"""
    
    @staticmethod
    def create_minimal_case(name: str = "minimal_test") -> dict:
        """创建最小用例"""
        return {
            "name": name,
            "description": f"测试用例: {name}",
            "env": {
                "base_url": "http://test.local",
                "username": "test_user",
                "password": "test_pass"
            },
            "steps": []
        }
    
    @staticmethod
    def create_case_with_steps(step_count: int = 3) -> dict:
        """创建包含步骤的用例"""
        steps = []
        for i in range(step_count):
            steps.append({
                "id": f"step_{i+1}",
                "type": "invoke" if i % 2 == 0 else "update_fields",
                "form_id": "test_form",
                "app_id": "test_app"
            })
        
        return {
            "name": f"case_with_{step_count}_steps",
            "description": f"包含{step_count}个步骤的测试用例",
            "env": {
                "base_url": "http://test.local",
                "username": "test_user",
                "password": "test_pass"
            },
            "steps": steps
        }
    
    @staticmethod
    def create_case_with_vars() -> dict:
        """创建包含变量的用例"""
        return {
            "name": "case_with_variables",
            "description": "包含变量的测试用例",
            "env": {
                "base_url": "http://test.local",
                "username": "test_user",
                "password": "test_pass"
            },
            "vars": {
                "test_number": "TEST${rand:6}",
                "test_name": "测试员${vars.test_number}",
                "test_date": "${today}"
            },
            "steps": [
                {
                    "id": "open",
                    "type": "open_form",
                    "form_id": "test_form",
                    "app_id": "test_app"
                },
                {
                    "id": "fill",
                    "type": "update_fields",
                    "form_id": "test_form",
                    "app_id": "test_app",
                    "fields": {
                        "number": "${vars.test_number}",
                        "name": {"zh_CN": "${vars.test_name}"},
                        "effect_date": "${vars.test_date}"
                    }
                },
                {
                    "id": "save",
                    "type": "invoke",
                    "form_id": "test_form",
                    "app_id": "test_app",
                    "ac": "save",
                    "key": "tbmain",
                    "method": "itemClick",
                    "args": ["save"]
                }
            ],
            "assertions": [
                {"type": "no_save_failure", "step": "save"},
                {"type": "no_error_actions", "step": "save"}
            ]
        }
    
    @staticmethod
    def create_case_with_assertions() -> dict:
        """创建包含断言的用例"""
        return {
            "name": "case_with_assertions",
            "steps": [
                {"id": "s1", "type": "open_form", "form_id": "f1", "app_id": "a1"},
                {"id": "s2", "type": "invoke", "form_id": "f1", "app_id": "a1", "ac": "save"}
            ],
            "assertions": [
                {"type": "no_save_failure", "step": "s2"},
                {"type": "no_error_actions", "last_step": True},
                {"type": "response_contains", "step": "s2", "needle": "success"}
            ]
        }
    
    @staticmethod
    def create_mock_har(entries_count: int = 5) -> dict:
        """创建模拟HAR数据"""
        entries = []
        
        for i in range(entries_count):
            entry = {
                "request": {
                    "url": f"http://test.local/form/batchInvokeAction.do?page={i}",
                    "method": "POST",
                    "headers": [
                        {"name": "Content-Type", "value": "application/json"}
                    ],
                    "postData": {
                        "mimeType": "application/json",
                        "text": json.dumps({
                            "formId": "test_form",
                            "appId": "test_app",
                            "actions": [
                                {"a": "u", "k": f"field_{i}", "v": f"value_{i}"}
                            ]
                        })
                    }
                },
                "response": {
                    "status": 200,
                    "content": {
                        "mimeType": "application/json",
                        "text": json.dumps([
                            {"a": "u", "k": f"field_{i}", "v": f"value_{i}"},
                            {"a": "showMessage", "args": ["操作成功"]}
                        ])
                    }
                },
                "time": 100 + i * 10
            }
            entries.append(entry)
        
        return {
            "log": {
                "version": "1.2",
                "creator": {"name": "TestDataFactory", "version": "1.0"},
                "entries": entries
            }
        }
    
    @staticmethod
    def create_mock_har_with_save() -> dict:
        """创建包含保存操作的HAR"""
        return {
            "log": {
                "version": "1.2",
                "entries": [
                    {
                        "request": {
                            "url": "http://test.local/form/batchInvokeAction.do",
                            "method": "POST",
                            "postData": {
                                "mimeType": "application/json",
                                "text": json.dumps({
                                    "formId": "test_form",
                                    "appId": "test_app",
                                    "actions": [
                                        {"a": "u", "k": "name", "v": "测试名称"},
                                        {"a": "u", "k": "number", "v": "TEST12345"}
                                    ]
                                })
                            }
                        },
                        "response": {
                            "status": 200,
                            "content": {
                                "mimeType": "application/json",
                                "text": json.dumps([
                                    {"a": "showMessage", "args": ["保存成功"]},
                                    {"a": "close"}
                                ])
                            }
                        }
                    },
                    {
                        "request": {
                            "url": "http://test.local/form/batchInvokeAction.do",
                            "method": "POST",
                            "postData": {
                                "mimeType": "application/json",
                                "text": json.dumps({
                                    "formId": "test_form",
                                    "appId": "test_app",
                                    "actions": [
                                        {"a": "itemClick", "key": "tbmain", "args": ["save"]}
                                    ]
                                })
                            }
                        },
                        "response": {
                            "status": 200,
                            "content": {
                                "mimeType": "application/json",
                                "text": json.dumps([
                                    {"a": "showMessage", "args": ["操作成功"]}
                                ])
                            }
                        }
                    }
                ]
            }
        }
    
    @staticmethod
    def create_error_response(error_type: str = "validation") -> list:
        """创建错误响应"""
        error_templates = {
            "validation": [
                {"a": "showErrMsg", "args": ["请填写名称"]},
                {"a": "setFocus", "k": "name"}
            ],
            "save_failure": [
                {"a": "showMessage", "args": ["成功数量：0，失败数量：1"]},
                {"a": "openForm", "formId": "bos_operationresult", "pageId": "op_001"}
            ],
            "duplicate": [
                {"a": "showErrMsg", "args": ["编码已存在"]}
            ],
            "permission": [
                {"a": "showErrMsg", "args": ["无操作权限"]}
            ]
        }
        return error_templates.get(error_type, error_templates["validation"])
    
    @staticmethod
    def create_success_response() -> list:
        """创建成功响应"""
        return [
            {"a": "showMessage", "args": ["操作成功"]},
            {"a": "close"}
        ]
    
    @staticmethod
    def write_yaml_file(path: Path, data: dict) -> None:
        """写入YAML文件"""
        try:
            import yaml
            with open(path, "w", encoding="utf-8") as f:
                yaml.dump(data, f, allow_unicode=True, sort_keys=False, default_flow_style=False)
        except ImportError:
            from lib.har_extractor import to_yaml
            path.write_text(to_yaml(data) + "\n", encoding="utf-8")
    
    @staticmethod
    def write_har_file(path: Path, data: dict) -> None:
        """写入HAR文件"""
        path.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")


# 便捷别名
Factory = TestDataFactory
