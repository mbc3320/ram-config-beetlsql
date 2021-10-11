package top.beanshell.beetlsql.service.impl;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.query.LambdaQuery;
import org.beetl.sql.mapper.BaseMapper;
import top.beanshell.beetlsql.model.pojo.BaseEntity;
import top.beanshell.common.exception.BaseException;
import top.beanshell.common.exception.code.GlobalStatusCode;
import top.beanshell.common.model.dto.BaseDTO;
import top.beanshell.common.service.ServiceI;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * base service impl
 * @author binchao
 */
@Slf4j
public abstract class CRUDServiceImpl<D extends BaseDTO, T extends BaseEntity> implements ServiceI<D> {

    /**
     * get dao instance
     * @return
     */
    protected abstract BaseMapper<T> getDao();

    /**
     * get current pojo of database table
     * @return
     */
    protected Class<T> currentModelClass() {
        return (Class<T>) getSuperClassGenericType(this.getClass(), 1);
    }

    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            log.warn(String.format("Warn: %s's superclass not ParameterizedType", clazz.getSimpleName()));
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    log.warn(String.format("Warn: %s not set the actual class on superclass generic parameter", clazz.getSimpleName()));
                    return Object.class;
                } else {
                    return (Class)params[index];
                }
            } else {
                log.warn(String.format("Warn: Index: %s, Size of %s's Parameterized Type: %s .", index, clazz.getSimpleName(), params.length));
                return Object.class;
            }
        }
    }

    @Override
    public boolean saveEntity(D dto) {
        T entity = BeanUtil.toBean(dto, currentModelClass());
        entity.init();
        getDao().insert(entity);
        return true;
    }

    @Override
    public boolean updateEntityById(D dto) {
        T entity = BeanUtil.toBean(dto, currentModelClass());
        entity.setUpdateTime(new Date());
        return getDao().updateTemplateById(entity) == 1;
    }

    @Override
    public D getById(Long id) {
        T entity = getDao().single(id);
        if (null == entity) {
            throw new BaseException(GlobalStatusCode.DATA_IS_NOT_EXIST);
        }
        return BeanUtil.toBean(entity, (Class<D>) getSuperClassGenericType(getClass(), 0));
    }

    @Override
    public boolean removeById(Long id) {
        return getDao().deleteById(id) == 1 ;
    }

    /**
     * 创建LambdaQuery
     * @return
     */
    protected LambdaQuery<T> createLambdaQuery() {
        return getDao().createLambdaQuery();
    }
}
