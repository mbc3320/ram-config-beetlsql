package top.beanshell.beetlsql.util;

import cn.hutool.core.bean.BeanUtil;
import org.beetl.sql.core.page.DefaultPageResult;
import org.beetl.sql.core.page.PageResult;
import top.beanshell.common.model.dto.PageResultDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * page util
 * @author beanshell
 */
public final class PageUtil {

    private PageUtil() {
    }

    /**
     * page result of database to dto
     * @param page  page instance
     * @param cls   dto class
     * @param <D>   dto class
     * @return      pageResultDTO instance
     */
    public static <D extends Serializable> PageResultDTO<D> getPageResult(PageResult page, Class<D> cls) {

        DefaultPageResult defaultPage = (DefaultPageResult) page;

        PageResultDTO<D> pageResult = new PageResultDTO<>();

        pageResult.setCurrent((int) defaultPage.getPage());
        pageResult.setPageSize(defaultPage.getPageSize());

        pageResult.setTotal(defaultPage.getTotalPage());
        pageResult.setTotal(defaultPage.getTotalRow());

        List<D> resultList = new ArrayList<>();
        for (Object data : defaultPage.getList()) {
            D result = BeanUtil.toBean(data, cls);
            resultList.add(result);
        }
        pageResult.setRecords(resultList);
        return pageResult;
    }
}
