/*
 * ═════ SOURCE ═════
 *   项目: 360度测评 (dcs/360度测评/)
 *   源文件: code/opmc-cea/src/main/java/cds0/opmc/cea/plugin/form/AssessActivityEdit.java (propertyChanged 方法节选)
 *   采集日期: 2026-04-24 · 单方法 ~10 行 · 本模板扩充成完整类 ~90 行
 *
 * ═════ INTENT ═════
 *   主 intent: field_property_changed_linkage (字段联动 / 字段选中触发其它字段)
 *   次 intent: add_main_field 的扩展 · 加字段后立刻要做联动时用这个
 *
 * ═════ VARIABLES ═════
 *   {{PACKAGE}}              · 包路径
 *   {{CLASS_NAME}}           · 类名（以 Edit / FormPlugin / BillPlugin 结尾）
 *   {{PARENT_CLASS}}         · 父类选择：AbstractFormPlugin / AbstractBillPlugIn / HRCoreBaseBillEdit / HRDataBaseEdit
 *   {{ISV_APP}}              · ISV 包名
 *   {{TRIGGER_FIELD}}        · 触发联动的字段 key（如 scoresystemmap 一个 boolean）
 *   {{TARGET_FIELD}}         · 被联动的字段 key（如 scoresystem 要被改必填态）
 *   {{TRIGGER_VALUE_MATCH}}  · 触发条件值（如 "1" 表示勾选）
 *
 * ═════ CAUTION ═════
 *   ⚠ propertyChanged 对所有字段变更都触发 · 必须用 e.getProperty().getName() 过滤
 *     → 生产代码最佳做法：先 if (key 不是我关心的) return
 *   ⚠ 改字段必填态：getControl(key) 拿 Control · 强转到具体控件类型（BasedataEdit / TextEdit / ComboEdit）
 *     → 然后 setMustInput(true/false)
 *   ⚠ 改字段值：getModel().setValue(key, value) · 不能直接 set entity · 否则 UI 不更新
 *   ⚠ 避免死循环：setValue 会再次触发 propertyChanged · 用 beginInit/endInit 包住（见 HR 导入 R4）
 *   ⚠ 父类选择：
 *       HRCoreBaseBillEdit     HR 单据表单（多数 HR 业务单都用这个）
 *       HRDataBaseEdit         HR 基础资料表单
 *       AbstractBillPlugIn     BOS 通用单据（非 HR）
 *       AbstractFormPlugin     BOS 通用表单（静态表单 / 动态表单）
 *   ⚠ super.propertyChanged(e) · 第一行必须调 · 保留框架默认行为
 */

package {{PACKAGE}};

import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.hr.hbp.common.util.HRBaseConstants;
import kd.hr.hbp.common.util.HRStringUtils;

/**
 * {{CLASS_NAME}}
 *
 * 字段联动 · 一个字段变更时修改另一个字段的必填态或值
 * 注册方式：BOS 设计器 → 表单 → 插件 · 绑定本类 FQN
 */
public class {{CLASS_NAME}} extends {{PARENT_CLASS}} {

    // 字段 key 常量化
    private static final String FIELD_TRIGGER = "{{TRIGGER_FIELD}}";
    private static final String FIELD_TARGET = "{{TARGET_FIELD}}";

    @Override
    public void propertyChanged(PropertyChangedArgs e) {
        super.propertyChanged(e);  // ⚠ 第一行必须调

        // ─── Step 1 · 按字段名过滤 · 不关心的字段直接返回 ─────
        String key = e.getProperty().getName();
        if (!FIELD_TRIGGER.equals(key)) return;

        // ─── Step 2 · 读当前值 · 按值分支处理 ─────
        String currentValue = String.valueOf(getModel().getValue(FIELD_TRIGGER));

        // ─── Step 3 · 控制目标字段的必填态 ─────
        // 示例：基础资料字段 → BasedataEdit
        BasedataEdit targetCtrl = (BasedataEdit) this.getControl(FIELD_TARGET);

        if (HRStringUtils.equals("{{TRIGGER_VALUE_MATCH}}", currentValue)) {
            // 条件匹配 → 目标字段改必填
            targetCtrl.setMustInput(Boolean.TRUE);
        } else {
            // 不匹配 → 改非必填
            targetCtrl.setMustInput(Boolean.FALSE);
        }

        // ─── 扩展：也可改值 · 但注意避免死循环 ─────
        // beginInit();
        // getModel().setValue(FIELD_TARGET, derivedValue);
        // endInit();

        // ─── 扩展：也可控可见性 ─────
        // this.getView().setVisible(Boolean.TRUE, FIELD_TARGET);
    }
}
