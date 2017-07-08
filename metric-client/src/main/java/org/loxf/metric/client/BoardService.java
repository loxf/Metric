package org.loxf.metric.client;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.BoardDto;
import org.loxf.metric.common.dto.PageData;

import java.util.Map;

/**
 * Created by caiyang on 2017/5/4.
 */
public interface BoardService {
    /**
     * 获取看板列表支持分页
     * @param board
     * @return
     */
    public PageData listBoradPage(BoardDto board);


    /**
     * 创建看板
     * @param boardDto
     * @return
     */
    public BaseResult<String> createBoard(BoardDto boardDto);


    /**
     * 根据看板Code获取看板实体信息
     * @param boardCode
     * @return
     */
    public BaseResult<BoardDto> getBoardByBoardCode(String boardCode);


    public BaseResult<String>  updateBoardByCode(String boardCode, Map<String, Object> setParam);

    /**
     * 删除看板
     * @param boardCode
     * @return
     */
    public  BaseResult<String>  delBoardByCode(String boardCode);
}
