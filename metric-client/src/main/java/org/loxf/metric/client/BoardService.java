package org.loxf.metric.client;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.BoardDto;
import org.loxf.metric.common.dto.PageData;

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
     * 根据看板Id获取看板实体信息
     * @param boardId
     * @return
     */
    public BaseResult<BoardDto> getBoardByBoardId(String boardId);


    public BaseResult<String>  updateBoard(BoardDto boardDto);

    /**
     * 删除看板
     * @param boardId
     * @return
     */
    public  BaseResult<String>  delBoard(String boardId);
}
