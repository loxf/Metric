import org.junit.Test;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.service.impl.ChartServiceImpl;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hutingting on 2017/7/10.
 */

public class Testaa {
    @InjectMocks
    private ChartServiceImpl chartService = new ChartServiceImpl();

    @Test
    public void testChartService(){
        int rownum=10;
        int totalRecords=1;
        int b=(int)(totalRecords % rownum == 0 ? totalRecords / rownum : totalRecords / rownum + 1);
        int c=0;

//        ChartDto chartDto=new ChartDto();
//        chartDto.setChartCode("htt");
//        chartDto.setChartDim("渠道");
//        chartDto.setCreateUserName("htt");
//        chartService.insertItem(chartDto);

    }

    @Test
    public void testPageList(){
        ChartDto chartDto=new ChartDto();
        chartDto.setChartCode("htt");
        chartDto.setChartDimension("channel_name");
        chartDto.setHandleUserName("htt");
        BaseResult<PageData> i=chartService.getPageList(chartDto);
        int a=0;

    }
}
