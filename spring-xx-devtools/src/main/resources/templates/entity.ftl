<#-- @ftlvariable name="imports" type="java.lang.String" -->
<#-- @ftlvariable name="entity" type="com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta" -->
package ${entity.entityPackageName};

${imports}

<#if entity.remarks??>
/**
 * ${entity.remarks}
 */
</#if>
@Getter
@Setter
public class ${entity.className} <#if entity.baseEntity??>extends ${entity.baseEntity.className}<#if entity.baseEntity.typeParameters??>&lt;${entity.baseEntity.renderTypeParameters(entity)}&gt;</#if></#if> {
<#list entity.fields as field>
  <#if field.remarks??>
  /**
   * ${field.remarks}
   */
  </#if>
  private ${field.javaType.className} ${field.propertyName};

</#list>
}
