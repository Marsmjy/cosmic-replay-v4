"""cosmic-replay skill - 苍穹表单协议回放框架"""
from .replay import (
    CosmicSession, CosmicFormReplay, login,
    CosmicError, LoginError, ProtocolError, BusinessError,
    find_actions, find_form_in_response, has_error_action,
)
from .diagnoser import (
    extract_save_errors, summarize_response, format_error_report,
    OPERATION_RESULT_FORMID,
)
from .field_resolver import FieldResolver

__all__ = [
    "CosmicSession", "CosmicFormReplay", "login",
    "CosmicError", "LoginError", "ProtocolError", "BusinessError",
    "find_actions", "find_form_in_response", "has_error_action",
    "extract_save_errors", "summarize_response", "format_error_report",
    "OPERATION_RESULT_FORMID",
    "FieldResolver",
]