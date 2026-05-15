import sys
sys.path.insert(0, r'c:\Users\kingdee\Desktop\cosmic-replay-v4\cosmic-replay-v4')

from lib.cosmic_login import login
import urllib3
import json
import requests

urllib3.disable_warnings()

# 配置
base_url = 'https://feature.kingdee.com:1026/feature_sit_hrpro'
username = '17299999999'
password = 'KDadm!@#2022'
datacenter_id = '2263950592869138432'

# 登录获取 session
print('[*] 正在登录苍穹平台...')
result = login(
    base_url,
    username,
    password,
    datacenter_id,
    proxies={'http': None, 'https': None}
)

print(f'[*] 登录结果: {result.get(" success\)}')
if not result.get('success'):
 print(f'[!] 登录失败: {result.get(\error\)}')
 sys.exit(1)

cookie = result['cookie']
csrf_token = result.get('csrf_token', '')
print(f'[+] 登录成功！Cookie: {cookie[:50]}...')
print(f'[+] CSRF Token: {csrf_token[:30]}...')

# 调用 getEntityType.do
entity_ids = ['haos_adminorgdetail', 'hom_onbrdinfo', 'bd_country']

headers = {
 'Cookie': cookie,
 'kd-csrf-token': csrf_token,
 'X-Requested-With': 'XMLHttpRequest',
}

for entity_id in entity_ids:
 url = f'{base_url}/metadata/getEntityType.do?entityId={entity_id}'
 print(f'\n{\=\*70}')
 print(f'实体: {entity_id}')
 print(f'URL: {url}')
 try:
 resp = requests.get(url, headers=headers, verify=False, timeout=15)
 print(f'Status: {resp.status_code}')
 print(f'返回长度: {len(resp.text)} 字符')
 print(f'返回内容 (前 3000 字符):')
 print(resp.text[:3000])
 print(f'...')
 except Exception as e:
 print(f'请求失败: {e}')
