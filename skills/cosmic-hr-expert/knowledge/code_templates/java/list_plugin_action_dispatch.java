/*
 * ═════ SOURCE ═════
 *   项目: 外部招聘 (dcs/外部招聘/)
 *   源文件: code/tdkw-tsc-tpsrm/src/main/java/tdkw/tsc/tpsrm/formplugin/JobListPlugin.java
 *   采集日期: 2026-04-24 · 原文件 ~42 行 · 本模板 ~70 行（加注释和 TODO）
 *
 * ═════ INTENT ═════
 *   主 intent: list_row_action_dispatch (列表行按钮/工具栏按钮业务)
 *   次 intent: list_data_permission (如果要做过滤 · 同 List 类 · 另一个方法 setFilter)
 *
 * ═════ VARIABLES ═════
 *   {{PACKAGE}}            · 包路径
 *   {{CLASS_NAME}}         · 类名（以 ListPlugin / List 结尾）
 *   {{ISV_APP}}            · ISV 包名
 *   {{ENTITY_FORM_NUMBER}} · 要操作的主实体 formNumber（如 tdkw_recruit_manage）
 *   {{TARGET_FIELD}}       · 要改的字段 key（如 tdkw_post_state）
 *
 * ═════ CAUTION ═════
 *   ⚠ afterDoOperation · 在操作完成后回调 · 适合"操作后更新 UI"或"后置动作"
 *     → 如果要拦截操作（不让它执行）· 用 beforeDoOperation + args.setCancel(true)
 *   ⚠ opKey 必须先在 BOS 设计器的列表工具栏里注册 · 本类才能感知到
 *     → 注册 opKey 要用 `addOperation` OpenAPI · 不要只改 XML
 *   ⚠ 列表批量操作 · 用 getSelectedRows().getPrimaryKeyValues() 拿主键
 *     → 单个记录：getSelectedRow() · null check
 *   ⚠ this.getView().updateView() · 刷新列表 · 不调这个用户看不到更新
 *   ⚠ 多 opKey 分流 · 用 switch-case 或 if-else · 代码结构清晰
 *
 * ═════ 相关 intent 补充 ═════
 *   如需"列表按数据权限过滤行" · 不是用 afterDoOperation · 是用 setFilter 事件：
 *
 *     @Override
 *     public void setFilter(SetFilterEvent evt) {
 *         // 追加 QFilter 实现行级过滤
 *         evt.getQFilters().add(new QFilter("enable", QCP.equals, "1"));
 *     }
 */

package {{PACKAGE}};

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * {{CLASS_NAME}}
 *
 * 列表插件 · 按 operateKey 分流处理选中行
 * 注册方式：BOS 设计器 → 列表 → 工具栏按钮 · 绑定本类 FQN
 */
public class {{CLASS_NAME}} extends AbstractListPlugin {

    @Override
    public void afterDoOperation(AfterDoOperationEventArgs args) {
        FormOperate source = (FormOperate) args.getSource();
        String operateKey = source.getOperateKey();

        // ─── Step 1 · 拿选中行主键 ──────────
        Object[] primaryKeyValues = this.getSelectedRows().getPrimaryKeyValues();
        if (primaryKeyValues == null || primaryKeyValues.length == 0) return;

        // ─── Step 2 · 批量加载选中数据 ──────────
        DynamicObject[] dynamicObjects = BusinessDataServiceHelper.load(
            "{{ENTITY_FORM_NUMBER}}",
            "id,{{TARGET_FIELD}}",                              // 按需补字段
            new QFilter[]{new QFilter("id", QCP.in, primaryKeyValues)});

        // ─── Step 3 · 按 opKey 分流 ──────────
        if (HRStringUtils.equals(operateKey, "{{OPKEY_OPEN}}")) {
            // 示例：批量置状态为 A
            for (DynamicObject d : dynamicObjects) {
                d.set("{{TARGET_FIELD}}", "A");
            }
            SaveServiceHelper.save(dynamicObjects);
            this.getView().updateView();
        } else if (HRStringUtils.equals(operateKey, "{{OPKEY_CLOSE}}")) {
            // 示例：批量置状态为 F
            for (DynamicObject d : dynamicObjects) {
                d.set("{{TARGET_FIELD}}", "F");
            }
            SaveServiceHelper.save(dynamicObjects);
            this.getView().updateView();
        }
        // 更多 opKey · 继续加分支
    }
}
