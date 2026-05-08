/*
 * ═════ SOURCE ═════
 *   项目: 编制管理 (dcs/编制管理/)
 *   源文件: code/tdkw-odc-dgdl/src/main/java/dgdl/odc/homs/opplugin/PositionValidator.java
 *   采集日期: 2026-04-24
 *
 * ═════ INTENT ═════
 *   主 intent: before_save_validation
 *   次 intent: field_uniqueness_validation · 任何"逐行校验 → 行级错误"场景
 *
 * ═════ VARIABLES ═════
 *   {{PACKAGE}}        · 你的 Java 包路径（如 tdkw.odc.homs.validator）
 *   {{CLASS_NAME}}     · Validator 类名（以 Validator 结尾 · 如 PositionValidator）
 *   {{FK_FIELD}}       · 要校验的外键字段名（如 adminorg）
 *   {{FK_FLAG_FIELD}}  · 外键实体上用于判断的标志字段（如 dgdl_isvirtual_ext）
 *   {{ISV_APP}}        · ISV 扩展包名
 *
 * ═════ CAUTION ═════
 *   ⚠ Validator 类必须挂到 OP 插件的 onAddValidators · 不能单独挂
 *     → 通常再配一个 XxxOp 类（参考模板 op_before_execute_validate.java） · 在 onAddValidators 里 args.addValidator(new XxxValidator())
 *   ⚠ addErrorMessage(rowDataEntity, msg) · 单行错误 · 框架自动定位到出错行
 *     → 不要用 throw KDBizException（OP 用那种 · Validator 用这种）
 *   ⚠ 错误文案必须 ResManager.loadKDString · 本模板源文件硬编码（原样记录） ·
 *     → 生产环境改成 ResManager.loadKDString("...", "{{CLASS_NAME}}_0", "{{ISV_APP}}")
 *   ⚠ 在 Validator 里只读不写 · 写字段应该在 OP 的 beforeExecuteOperationTransaction
 */

package {{PACKAGE}};

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.resource.ResManager;
import kd.bos.entity.ExtendedDataEntity;
import kd.bos.entity.validate.AbstractValidator;

/**
 * {{CLASS_NAME}}
 *
 * 逐行校验 · 单条记录业务规则
 * 注册方式：在对应 XxxOp 的 onAddValidators(AddValidatorsEventArgs args) 里加
 *     args.addValidator(new {{CLASS_NAME}}());
 */
public class {{CLASS_NAME}} extends AbstractValidator {

    // 字段 key 常量化（推荐 · 便于 rename 维护）
    private static final String {{FK_FIELD_CONST}} = "{{FK_FIELD}}";

    @Override
    public void validate() {
        for (ExtendedDataEntity rowDataEntity : this.getDataEntities()) {
            DynamicObject dataEntity = rowDataEntity.getDataEntity();
            DynamicObject fkObj = dataEntity.getDynamicObject({{FK_FIELD_CONST}});

            if (fkObj == null) {
                continue;  // 无关联 · 跳过本行
            }

            // ─── 业务条件 · 这里示范：外键实体带 {{FK_FLAG_FIELD}} 为 true 时不允许 ─────
            if (fkObj.getBoolean("{{FK_FLAG_FIELD}}")) {
                String msg = ResManager.loadKDString(
                    "虚拟组织%1$s下不能创建岗位",   // 默认中文
                    "{{CLASS_NAME}}_0",          // ResId · 类名_序号
                    "{{ISV_APP}}",               // 项目常量
                    new Object[]{ fkObj.getString("number") }
                );
                this.addErrorMessage(rowDataEntity, msg);
            }
        }
    }
}
