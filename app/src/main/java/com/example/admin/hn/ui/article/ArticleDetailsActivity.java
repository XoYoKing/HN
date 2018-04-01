package com.example.admin.hn.ui.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 文章详情
 *
 * @author Administrator
 */
public class ArticleDetailsActivity extends BaseActivity {
    @Bind(R.id.text_title_back)
    TextView textTitleBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_content)
    TextView tv_content;
    private int id;
    private String title;

    private String content = "摘要：看到企业纷纷带着备选方案来参加调研，长宁区大调研办公室负责人江轶群明白，这些跨部门、跨地域、跨事权的“三跨”问题并非凭借临空园区或是长宁区一己之力就能解决，需要市、区两级部门的协同合作。他现场向企业承诺，对于当天现场给不了明确答复的问题，区大调研办梳理后会上报市相关部门，从全市层面寻求解决路径。\n" +
            "\n" +
            "3月22日下午，长宁区建交系统“一委五局”的6位负责人齐刷刷出现在位于福泉北路33号的联合利华北亚区总部。这是今年全市开展大调研以来，上述负责人第11次集体出现，此前两个多月中，他们已组团走访了长宁全部10个街镇。这次来到长宁最西侧的虹桥临空经济园区，联合利华亚太区税务副总监姚键提出的第一个问题，就让六位负责人都捏了把汗。\n" +
            "\n" +
            "联合利华北亚区总部\n" +
            "“联合利华2006年初入临空园区时只有600名员工，办公园区按照1000人的体量建设，但如今员工总数已将近2000多人，中国区总部也升级为北亚总部，办公面积越来越捉襟见肘。”姚键提出，企业希望能在有限的土地面积中提高建筑容积率。\n" +
            "一旁的长宁区规划和土地管理局局长郭海马上现场回复：从土地集约利用角度来说，应该提升土地利用率；但临空地理位置特殊，有虹桥机场的限高要求，规土局会协助企业做好容积率需求评估，完善空间功能不足。\n" +
            "在联合利华亚太研发中心，树梢上冒出的新芽、早开的樱花和错落的亭台令整个办公园区宛若世外桃源。但正如姚键所说，不到1.0的容积率已远不相称。企业人数增加，可利用空间紧张，最直观的体现就是停车位紧缺。\n" +
            "“我们在福泉北路上申请了几十个停车位，但现在面临两个难题，一是市政道路车位属先到先得，其他企业车辆停靠后，我们的员工就又无处停车。”姚键更担心的是公司对面的临空一号公园建成后路边停车位将取消，员工们的停车难会再次加重。\n" +
            "“你们现在缺多少停车位？”长宁区建交党工委书记、区建交委主任赵成樑问企业。\n" +
            "“目前排队登记车位的员工有70到80人。”姚键提出建议，联合利华可否利用自己园区内的下沉式广场建造地下停车库，建成后的临空一号公园配套停车位可否在工作日开放给周边企业使用。\n" +
            "长宁区绿化市容局局长胡岗主动回应：“临空一号公园 建成后有250多个车位，主要考虑在举办大型音乐会时供游客使用，你提的建议很好，工作日的确可以研究开放给企业错峰停车，我们会与临空办进行协调。”\n" +
            "\n" +
            "福泉路金钟路路口\n" +
            "“说到停车难，原本都找建交委或者交警，没想到其实应该先找绿化局，还好这次各家单位一起来，太有针对性了！”看到停车问题得到多个部门的现场回应，虹桥临空经济园区办公室主任朱平对自己要反映的一串“疑难杂症”有了底气。\n" +
            "“为什么园区北部的白领都要开车上班？因为唯一的轨交站点淞虹路站距离太远，跨苏州河也没有直达临空的市政桥梁，交通不便问题一直存在。”朱平快人快语提出，优化交通组织能够明显改善临空园区的营商环境“短板”。每天早高峰进临空难、晚高峰出临空更难的情况已经持续多时，也让在园区工作了十几年的“老临空人”朱平发愁。这次成为“被调研对象”，他提出了思索许久的三个建议——\n" +
            "企业各自安排接驳大巴后，早晚高峰临空园区就宛如长途客运站，是否能请建交委与交警部门协调，持续从源头疏导企业班车有序停靠；西临空地区没有公交线路，外环400米林带公园和慢行步道年底建成后，大量私家车前往恐加剧交通压力，能否优化西临空地区公交系统，尽快恢复北翟路地面交通，缓解临空园区交通动线被“腰斩”的窘境。\n" +
            "\n" +
            "轨交淞虹路站外的公交车站\n" +
            "“另外，可否请建交委支持提供指挥交通的数据分析，帮助临空园区试验智慧停车、智慧交通信号灯等设施，用智能技术优化交通组织。”\n" +
            "听到这里，正逐条记录的长宁区建交委副主任吴道群马上停笔回应：“跨苏州河的云岭西路桥长宁段腾地已经完成，目前正与相关部门协调下一步方案论证。”“联合利华70个停车位的需求基本可以协调，建交委也会为其他有停车难的企业提供解决样本。”\n" +
            "临空园区开发建设中心主任林文欢主动拿起了话筒：“联合利华、江森自控、博世中国等总部型企业各自都有几十辆接驳车，能否与交警、公交部门协调，每天将公交车站与企业班车‘共享’几个小时，缓解早晚高峰园区停车压力。”\n" +
            "看到企业纷纷带着备选方案来参加调研，长宁区大调研办公室负责人江轶群明白，这些跨部门、跨地域、跨事权的“三跨”问题并非凭借临空园区或是长宁区一己之力就能解决，需要市、区两级部门的协同合作。他现场向企业承诺，对于当天现场给不了明确答复的问题，区大调研办梳理后会上报市相关部门，从全市层面寻求解决路径。\n" +
            "记者了解到，长宁区坚持将大调研与实际工作结合，“问需”与“问计”相结合，依托全区原有的信息系统逐一派单解决问题。下一步，长宁还将在财政、党群等部门系统内组成联合调研团队，挖掘企业、居民区、社会组织等反映集中的痛点难点，实现“以调研促改革”的最终目标。";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
    }

    @Override
    public void initTitleBar() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        textTitle.setText("文章详情");
        if (title != null) {
            tv_title.setText(title);
        }
        textTitleBack.setBackgroundResource(R.drawable.btn_back);
    }
    /**
     *
     */
    public static void startActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, ArticleDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @OnClick(R.id.text_title_back)
    public void onClick(View v) {
        if (v.getId() == R.id.text_title_back) {
            finish();
        }
    }

    @Override
    public void initData() {
        super.initData();
        tv_content.setText(content);
    }
}
