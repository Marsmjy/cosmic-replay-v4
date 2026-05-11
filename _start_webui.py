"""启动 webui 服务（绕过 Windows 下 python -m 的 sys.path 坑）"""
import sys
from pathlib import Path
ROOT = Path(__file__).resolve().parent
sys.path.insert(0, str(ROOT))
from lib.webui.server import main
main()
