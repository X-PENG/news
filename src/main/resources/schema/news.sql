/*
 Navicat Premium Data Transfer

 Source Server         : 云服务器mysql_PENG
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : 47.99.61.90:3306
 Source Schema         : news

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 19/04/2021 23:12:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `title` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '新闻标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '新闻正文',
  `column_id` int(0) NULL DEFAULT NULL COMMENT '外键，新闻栏目id，新闻发布时必须选择新闻栏目',
  `img_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片来源',
  `article_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文字来源',
  `news_status` tinyint(0) NOT NULL COMMENT '新闻状态。0-草稿，1-上传成功（备注：属于中转新闻），2-审核中，3-审核失败（备注：属于中转新闻），4-审核成功，待发布，5-已发布，6-撤销发布，7-打回修改（备注：属于中转新闻）',
  `is_carousel` tinyint(0) NOT NULL DEFAULT 0 COMMENT '新闻是否轮播发布，默认false',
  `set_carousel_time` datetime(0) NULL DEFAULT NULL COMMENT '新闻设置轮播的时机。如果很多新闻都设为轮播，则轮播顺序由该字段决定',
  `is_headlines` tinyint(0) NOT NULL DEFAULT 0 COMMENT '新闻是否作为头条，默认false',
  `set_headlines_time` datetime(0) NULL DEFAULT NULL COMMENT '新闻设置为头条的时机。如果很多新闻都设为头条，则谁作为头条由该字段决定',
  `is_image_news` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否是图片新闻',
  `is_top` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否置顶。表示是否在所在的新闻栏目置顶',
  `set_top_time` datetime(0) NULL DEFAULT NULL COMMENT '新闻设置置顶的时机。如果一个栏目多个新闻都设置了置顶，就按该时间决定先后顺序',
  `external_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外网链接，如果新闻是其他网站的链接，则设置外链',
  `inputter_id` int(0) NOT NULL COMMENT '外键，新闻稿的录入人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新闻的创建时间',
  `complete_input_time` datetime(0) NULL DEFAULT NULL COMMENT '新闻录入完成的时间',
  `editors` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与编辑的人员列表，逗号分隔',
  `reviewers` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与审核的人员列表，逗号分隔',
  `submitter_id` int(0) NULL DEFAULT NULL COMMENT '外键，提交审核的人，即送审人id',
  `submit_time` datetime(0) NULL DEFAULT NULL COMMENT '送审时间',
  `latest_editor_id` int(0) NULL DEFAULT NULL COMMENT '外键，最近修改新闻的用户id，为了让编辑能够知道最近谁修改了新闻',
  `latest_edit_time` datetime(0) NULL DEFAULT NULL COMMENT '最近修改时间',
  `current_review_epoch` tinyint(0) NOT NULL DEFAULT 0 COMMENT '新闻当前所处的审核轮次',
  `previous_epoch_review_pass_time` datetime(0) NULL DEFAULT NULL COMMENT '当新闻通过一轮审核，就设置该时间，表示这一轮审核通过的时间，下一轮审核的人就可以看到；如果新闻处于待发布状态，则表示新闻通过终审的时间',
  `publisher_id` int(0) NULL DEFAULT NULL COMMENT '外键，新闻发布人的id',
  `real_pub_time` datetime(0) NULL DEFAULT NULL COMMENT '真实的发布时间',
  `show_pub_time` datetime(0) NULL DEFAULT NULL COMMENT '对外显示的发布时间',
  `init_reading_count` int(0) NOT NULL DEFAULT 0 COMMENT '新闻初始阅读量',
  `real_reading_count` int(0) NOT NULL DEFAULT 0 COMMENT '新闻实际阅读量',
  `extra` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储额外的信息（json格式）。比如，审核失败时，保存审核意见；新闻作为轮播图发布时，保存封面图片地址；新闻被打回修改时，保存修改意见',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (3, '邱水平率团赴上海、浙江、湖南、江西 推进北大与地方合作', '<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;3月29日至4月2日，北京大学党委书记邱水平率团访问上海市、浙江省、湖南省、江西省，拜会主要领导、推进校地合作项目、考察地方研究院建设，并出席选调生座谈会。北京大学党委常委、常务副校长兼教务长、中国科学院院士龚旗煌，党委常委、副校长王博，党委常委、副校长、中科院院士张平文，党委常委、副校长、中科院院士黄如，校长助理、秘书长孙庆伟等出席有关活动。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;3月29日，北京大学与中国（上海）自由贸易试验区临港新片区管理委员会就&ldquo;北京大学上海临港国际科技创新中心&rdquo;项目进行合作签约。龚旗煌与临港新片区党工委委员、专职副主任吴晓华代表双方签署合作项目协议。</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://news.pku.edu.cn/images/2021-04/1f637c857f0c4132ae86c2e5cad6eec5.jpeg\" width=\"396\" height=\"241\" /></p>\n<p style=\"text-align: center;\">签约现场</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;签约后，邱水平一行拜会上海市委书记李强。李强表示，上海和北京大学都传承着红色基因，肩负着光荣使命，承担着党中央赋予的一系列重大国家战略任务。他希望以此次签约为契机，进一步推动市校合作走深走实，加强协同、优势互补、各扬其长，聚焦国际前沿领域和国家战略需求，在强化创新策源功能、培养集聚高端人才、创新科技体制机制等方面深化务实合作。李强欢迎北大深度参与上海发展，上海将营造良好环境、提供优质服务，推动双方合作早日结出丰硕成果。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;邱水平表示，北京大学与上海有着良好合作基础，进入新发展阶段，双方交流对接更加紧密、合作空间更为广阔。北大将充分依托上海优势、发挥自身作用，加快推动临港国际科技创新中心项目建设，拓展深化基础研究、科技创新、产业发展、人文社科等各领域合作，更好融入和参与上海&ldquo;十四五&rdquo;乃至更长时期的发展，在共同服务国家战略中实现更大发展。</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" title=\"d13a79baff2f43a9809671884b7fb3f5.jpg\" src=\"https://news.pku.edu.cn/images/2021-04/d13a79baff2f43a9809671884b7fb3f5.jpg\" alt=\"d13a79baff2f43a9809671884b7fb3f5.jpg\" width=\"550\" height=\"365\" border=\"0\" vspace=\"0\" /></p>\n<p style=\"text-align: center;\">李强与邱水平会见</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;在沪期间，邱水平一行还前往中国共产党发起组成立地（《新青年》编辑部）旧址、中国（上海）自由贸易试验区临港新片区考察。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;3月30日，邱水平一行拜会浙江省委书记袁家军。袁家军表示，近些年来，浙江与北京大学携手共进，构建紧密交流协调机制，开展多领域全方面合作，共同建设创新平台，取得了有效成果。他希望双方充分发挥各自优势，在更多领域、更深层次开展务实合作，特别是共同推进浙江省三大科创高地、共同富裕示范区建设。袁家军欢迎更多优秀北大师生到浙江创业创新，浙江一定提供最佳的服务、最优的环境，为他们干事创业搭建宽阔的舞台。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;邱水平表示，北京大学与浙江有着独特渊源，近年来一直保持着密切的交流与合作，省校合作取得积极成效。面向&ldquo;十四五&rdquo;发展，北大将认真落实好双方战略合作协议，进一步加强与浙江全方位合作，全力支持和助力浙江高质量发展，努力推动省校合作结出更加丰硕的成果。</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" title=\"eda3084f0ea04e9299a93d94f46b4e1f.jpg\" src=\"https://news.pku.edu.cn/images/2021-04/eda3084f0ea04e9299a93d94f46b4e1f.jpg\" alt=\"eda3084f0ea04e9299a93d94f46b4e1f.jpg\" width=\"550\" height=\"377\" border=\"0\" vspace=\"0\" /></p>\n<p style=\"text-align: center;\">袁家军与邱水平会见</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;3月31日上午，浙江省政府与北京大学续签《浙江省人民政府北京大学战略合作协议》。根据协议，双方将发挥各自优势，整合资源，在战略决策咨询、高能级创新载体建设、科技创新与成果转化、人才干部交流培养等方面开展全方位合作。浙江省委副书记、省长郑栅洁与邱水平见证签约，并共同为数字视频编解码技术国家工程实验室杭州基地、高可信软件技术教育部重点实验室杭州基地、国家集成电路产教融合创新平台杭州基地揭牌。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;在杭期间，邱水平一行还考察调研了北京大学信息技术高等研究院、教育基金会长三角发展办公室，出席了北京大学浙江省选调生座谈会。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;4月1日，邱水平一行拜会湖南省委书记许达哲。许达哲表示，当前，湖南正深入贯彻习近平总书记考察湖南重要讲话精神，深入开展党史学习教育，大力实施&ldquo;三高四新&rdquo;战略，需要有力的科技和人才支撑。他希望北京大学在党史研究、科技创新平台建设、数字经济发展等方面与湖南进一步加强合作，助推湖南走在中部地区崛起前列。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;邱水平表示，湖南文化底蕴深、高等教育实力强、先进制造业发展成效好，与北京大学合作空间广阔。北京大学愿与湖南加强合作，共同进行实践基地建设，助力湖南打造人才高地。</p>\n<p style=\"text-align: center;\"><img title=\"8feaa4e619a643b182d688f265bd0d79.jpg\" src=\"https://news.pku.edu.cn/images/2021-04/8feaa4e619a643b182d688f265bd0d79.jpg\" alt=\"8feaa4e619a643b182d688f265bd0d79.jpg\" width=\"550\" height=\"342\" border=\"0\" vspace=\"0\" /></p>\n<p style=\"text-align: center;\">许达哲与邱水平会见</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;同日，邱水平一行会见了湖南省委常委、长沙市委书记吴桂英。吴桂英表示，北京大学与长沙历史渊源深厚，在科技教育人才文化等方面合作基础良好、前景广阔。她希望在良好合作基础上，构建更加紧密的市校交流合作机制，开展多领域深层次合作，在战略决策咨询、科技创新、人才培养培训等领域取得新成果、开创新局面。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;邱水平表示，北京大学与长沙保持着密切的交流合作。立足新发展阶段，共同肩负历史使命、面临发展机遇，北京大学将与长沙携手服务国家战略实施，深化市校合作，创新体制机制，推动形成稳定长期的全面战略合作关系、结出更加丰硕的合作成果，形成高校服务地方发展、地方助推高校创新的良好格局。</p>\n<p style=\"text-align: center;\"><img title=\"993e5270c0424bfc801c9308789f9f89.jpg\" src=\"https://news.pku.edu.cn/images/2021-04/993e5270c0424bfc801c9308789f9f89.jpg\" alt=\"993e5270c0424bfc801c9308789f9f89.jpg\" width=\"550\" height=\"328\" border=\"0\" vspace=\"0\" /></p>\n<p style=\"text-align: center;\">吴桂英与邱水平会见</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;在长沙期间，邱水平一行还赴湖南第一师范城南书院校区毛泽东与第一师范纪念馆考察。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;4月2日，邱水平一行赴南昌出席北京大学南昌创新研究院揭牌仪式。邱水平与江西省委常委、省委组织部部长刘强为创新研究院揭牌。张平文在揭牌仪式上致辞。揭牌仪式由南昌市委副书记、市长万广明主持。</p>\n<p style=\"text-align: center;\"><img title=\"a7930f45b0e743e78b96877a5c71b11e.jpg\" src=\"https://news.pku.edu.cn/images/2021-04/a7930f45b0e743e78b96877a5c71b11e.jpg\" alt=\"a7930f45b0e743e78b96877a5c71b11e.jpg\" width=\"550\" height=\"367\" border=\"0\" vspace=\"0\" /></p>\n<p style=\"text-align: center;\">揭牌仪式现场</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;北京大学南昌创新研究院由南昌市和北京大学合作共建，落户于南昌高新区的江西省高层次人才产业园。北京大学南昌创新研究院作为江西省、南昌市与北京大学合作实施载体，定位为高水平新型研发机构。研究院落户后，将依托南昌航空产业优势，聚焦航空技术、新材料和先进制造三个领域，打造集技术研发、成果转化、产业孵化、人才培养于一体的产学研用新型创新平台。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;上海市及临港新片区有关部门、浙江省及杭州市有关部门、湖南省及长沙市有关部门、江西省及南昌市有关部门负责同志，北京大学医学部、信息科学技术学院、材料科学与工程学院、化学与分子工程学院、工学院、未来技术学院、党委办公室校长办公室、科技开发部、教育基金会、国内合作委员会办公室、学生就业指导服务中心有关负责同志参加相关活动。</p>', NULL, '北京大学新闻网', '北京大学新闻网', 0, 0, NULL, 0, NULL, 0, 0, NULL, NULL, 1, '2021-04-13 18:49:48', NULL, NULL, NULL, NULL, NULL, NULL, '2021-04-13 23:35:12', 0, NULL, NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `news` VALUES (4, '故宫院长王旭东来访北大并举办讲座', '<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;4月9日，故宫博物院院长王旭东、副院长赵国英带领相关部门负责人来访北京大学，并与北京大学校长郝平、副校长王博、校长助理孙庆伟以及相关院系领导、负责人在临湖轩举行座谈。会谈后，王旭东为北大师生带来&ldquo;敦煌与故宫&rdquo;主题讲座。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;郝平对王旭东一行的到来表示欢迎。他指出，北京大学与故宫博物院签署战略合作协议以来，双方积极落实、推进合作意向，目前合作取得了一定的进展，希望未来可以开展更加全面、全方位的合作。北京大学非常希望能在中华文明与历史教育、学术交流与人才培养等方面得到故宫博物院的大力支持。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;王旭东对北大在落实推进双方合作方面给予的支持表示感谢。他指出，故宫博物院真诚地希望能在学术研究、文化遗产保护与传播等领域与北京大学开展合作，得到北京大学的支持与帮助，吸引年轻一代学子投身文博事业。近年来，不少文博系统的专家学者走进高校，实现了高校与博物馆之间的人才流通。故宫博物院希望能与北京大学强强联合，相互支持，共同培养学术人才，努力实现时代赋予的使命。</p>\n<p style=\"text-align: center;\"><img title=\"\" src=\"http://news.pku.edu.cn/images/2021-04/fe9f090f5685446bb98d6f3bc0dbb07d.jpeg\" width=\"417\" height=\"280\" /></p>\n<p style=\"text-align: center;\">合影留念</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;座谈结束后，王旭东在王博的陪同下前往二体地下报告厅，为北大师生带来了一场题为&ldquo;敦煌与故宫&mdash;&mdash;中华文化包容互鉴的结晶&rdquo;的学术讲座。该场讲座是北京大学人文社会科学研究院（简称文研院）与故宫博物院联合举办的&ldquo;故宫与故宫学&rdquo;系列讲座的第五讲，也是新学期以来的首讲。300多位北大师生在现场聆听。讲座由文研院院长、北京大学历史学系博雅讲席教授邓小南主持。</p>\n<p style=\"text-align: center;\"><img title=\"\" src=\"http://news.pku.edu.cn/images/2021-04/214180c5c75e4860936b5e924e644cd3.jpeg\" width=\"416\" height=\"278\" /></p>\n<p style=\"text-align: center;\">讲座现场</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;讲座中，王旭东从敦煌与故宫文化的艺术价值、形成来源、发展历程、传承历史以及保护工作等层面深刻阐释了敦煌与故宫的联系和差异，并就敦煌与故宫在文化上共同体现的不同民族间文化交流融合的重要意义与听众进行交流。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;讲座从敦煌与故宫两大文化遗产的形成背景及历史沿革展开。王旭东认为，敦煌保留了&ldquo;灵动&rdquo;&ldquo;变化&rdquo;的艺术形态，精美的石窟彩塑、丰富的壁画内容描摹出不同时期人们生活的方方面面，深刻反映了特定时期的审美特征。彩塑和壁画共同展现了敦煌文化的艺术价值，持续扩大了丝绸之路延展过程中文化交流与传播所产生的巨大影响力。故宫文化则处处体现&ldquo;厚重感&rdquo;，其折射的不仅仅是五千年以来中华民族优秀传统文化的积淀成果，更充分流露出中西文化交流碰撞后的多元色彩。</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-04/b6d15782df4941d88ec5133bdc2df679.jpeg\" width=\"416\" height=\"278\" /></p>\n<p style=\"text-align: center;\">王旭东做讲座</p>\n<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在比较敦煌和故宫在文化意义上的相通与相异之处时，王旭东指出，敦煌莫高窟和故宫两大文化殿堂是民间信仰和国家支持两种力量共同推动、中外文化交流与中国内部不同民族交融所产生的，可以从中清楚地看到国家力量与民间力量的交互作用。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;王旭东以丰富的图片和史料信息，讲述了敦煌与故宫文物保护工作的基本情况与漫漫历程。从张大千、于右任等有识之士为壁画保护积极奔走，到常书鸿等前辈放弃优渥生活扎根敦煌莫高窟，再到新中国成立后，在国家和社会各界的支持下，洞窟清沙、加固工程持续开展，一代又一代文物工作者前赴后继，为敦煌保护殚精竭虑，倾尽一生心血。随着保护思想、修复手段日趋完善，敦煌也逐渐呈现新的面貌。故宫博物院自开院以来，无论是持续的文物整理与修缮、还是抗日战争文物南迁的艰难历程，都体现了以国家力量为主导的对文物保护的高度重视与大力投入。新中国成立后，故宫的修缮及文物清点工作一直有序地展开。故宫与时代同步，在文物修缮、文物安全、文物展示等方面不断推陈出新，近年来更是利用科技手段、管理手段等打造&ldquo;平安故宫&rdquo;&ldquo;学术故宫&rdquo;&ldquo;数字故宫&rdquo;&ldquo;活力故宫&rdquo;，让文物真正&ldquo;活起来&rdquo;，践行&ldquo;完整地保护并负责任地传承弘扬故宫承载的中华优秀传统文化&rdquo;的初心。王旭东希望北京大学与故宫进一步完善互访机制，在一次次故宫参访中传播故宫文化，真正肩负起保护故宫文化的社会责任。</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-04/a290bac1b1284eb7969c7eaf063f2945.jpeg\" width=\"416\" height=\"269\" /></p>\n<p style=\"text-align: center;\">师生认真聆听</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;讲座最后，王旭东强调，新中国成立后敦煌与故宫的保护发展启示我们应坚定文化自信，以更加宽阔的胸怀、更加开放包容的心态吸收人类文明成果，建设文化强国，践行构建人类命运共同体的伟大理念。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;互动交流环节，王旭东就听众提出的&ldquo;故宫文化爱好者如何对故宫文化传播作出贡献&rdquo;&ldquo;流失文物如何回归&rdquo;等问题与大家进行交流讨论。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;邓小南在主持讲座时表示，王旭东的讲座内容详实、视野宏阔、视角独特，生动的讲述加深了现场师生的文化体验。文研院将与故宫一道，为双方的合作探寻新的途径与发展模式，共同为促进中华民族优秀文化创新发展作出贡献。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;在北京大学开启&ldquo;故宫与故宫学&rdquo;系列讲座的同时，故宫博物院也开展了&ldquo;北京大学系列学术讲座&rdquo;，双方互邀知名专家学者就故宫学、清代宫廷史、历史学、古书画等重点领域开设讲坛。2020年春季学期，故宫与北大合作开设文研课程&ldquo;中国古代书画鉴定与鉴藏&rdquo;&ldquo;明清宫廷建筑与宫殿陈设&rdquo;。故宫专家将故宫特色学术研究引入课堂，实现故宫学术进北大。在北大与故宫的共同努力下，双方的学术交流合作不断深化，并逐渐迈上新的层次。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;接下来，&ldquo;故宫与故宫学&rdquo;系列讲座还将继续。北京大学和故宫博物院的相关专家、学者将围绕博物馆学、古文献学、古书画、宋代文学、清宫典仪等领域展开精彩讲座。具体讲座信息将在文研院公众号、&ldquo;故宫研究院&rdquo;公众号上发布。</p>', NULL, '北京大学新闻网', '北京大学新闻网', 0, 0, NULL, 0, NULL, 0, 0, NULL, NULL, 1, '2021-04-13 18:53:30', NULL, NULL, NULL, NULL, NULL, NULL, '2021-04-13 18:53:31', 0, NULL, NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `news` VALUES (5, '自强成就卓越 创新塑造未来——清华大学110周年校庆献辞', '<p class=\"vsbcontent_start\"><strong>●&nbsp;</strong>校长<strong>&nbsp;邱勇&nbsp;</strong>校党委书记<strong>&nbsp;陈旭</strong></p>\n<p style=\"text-align: center;\"><img class=\"img_vsb_content\" title=\"\" src=\"https://news.tsinghua.edu.cn/__local/D/36/45/E652F28EF6733C73B8B4DEE5B56_6B6571FC_1F4A1.jpg\" width=\"554\" /></p>\n<p class=\"vsbcontent_start\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;清和四月，树木华滋。在清华大学即将迎来110周年华诞的喜庆时刻，我们代表学校向全校师生员工和海内外校友致以节日的问候！向长期关心支持学校事业发展的各级领导部门、各单位和各界朋友表示衷心的感谢！</p>\n<p><strong>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;西山苍苍，东海茫茫</strong>。110年前，清华诞生于中国内忧外患、山河破碎的深重苦难中。自建校起，自强不息的民族精神、&ldquo;耻不如人&rdquo;的不屈品格就深深融入清华人的血脉里。清华人秉持科学救国理想，将清华从一所留美预备学校建设成为一所人民的大学、一所实现了教育与学术自立自强的现代大学。从&ldquo;一二&middot;九&rdquo;运动的一声呐喊到西南联大的&ldquo;八音合奏&rdquo;，从新中国成立后建设多科性工业大学的积极探索到乘着改革开放春风吹响创建世界一流大学的号角，清华大学在长期办学实践中形成了深厚的文化积淀和光荣的革命传统。<strong>&ldquo;自强不息、厚德载物&rdquo;的校训、&ldquo;行胜于言&rdquo;的校风、&ldquo;严谨、勤奋、求实、创新&rdquo;的学风、&ldquo;爱国奉献、追求卓越&rdquo;的精神，塑造了清华人的意志品质，涵养了清华人的气度风范。一代代清华人接续奋斗，为祖国、为人民、为民族建立了突出功绩，走出了一条扎根中国大地建设世界一流大学之路</strong>。</p>\n<p><strong>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;春风化雨，其乐未央。清华始终将育人置于最重要、最根本的位置</strong>。1911年，《清华学堂章程》中规定&ldquo;以培植全材，增进国力为宗旨&rdquo;。梅贻琦校长提出&ldquo;从游论&rdquo;，强调&ldquo;发展全人格&rdquo;&ldquo;通识为本&rdquo;。新中国成立后，蒋南翔校长倡导&ldquo;又红又专、全面发展&rdquo;&ldquo;因材施教&rdquo;的教育理念，大力推进政治辅导员、科学登山队、文艺与体育&ldquo;三支学生代表队&rdquo;的建设。改革开放后，学校坚持高素质、高层次、多样化、创造性的人才培养目标，致力于培养学生具备健全人格、宽厚基础、创新思维、全球视野和社会责任感，引导学生成长为肩负使命、追求卓越的时代新人。<strong>来自五湖四海的莘莘学子在宁静优美、生机勃勃、昂扬向上的清华园里度过了难忘的青春时光并奔向&ldquo;祖国最需要的地方&rdquo;，从中涌现出了一大批治学、兴业、治国的优秀人才</strong>。</p>\n<p><strong>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;致知穷理，学问笃实</strong>。1925年，清华成立大学部并设国学研究院，欲&ldquo;建设最高等学术&rdquo;。无论是开创融会中西、古今、文理的学术风格，还是确立&ldquo;顶天、立地、树人&rdquo;的科研宗旨；无论是参与&ldquo;两弹一星&rdquo;、密云水库等重大工程，承担国徽、人民英雄纪念碑等重要设计，还是创造高温气冷堆、大型集装箱检测系统等重要成果；无论是开辟中国现代诸多科学技术与文化艺术学术领域，兴办一批国家现代化建设亟需的新兴专业，还是明确综合性、研究型、开放式的总体办学思路，<strong>清华大学面向世界学术前沿和国家重大战略需求，填补了一项又一项空白，取得了一个又一个突破，发展成为我国科学技术和思想文化创新的重要基地</strong>。</p>\n<p><strong>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;人文日新，行健不息</strong>。进入新时代，清华大学开启了一场使命驱动的改革。<strong>学校坚持和加强党的全面领导，将世界一流、中国特色、清华风格统一在办学实践中</strong>。以教师队伍人事制度改革为突破口，深入实施人才强校核心战略。深化教育教学改革，确立价值塑造、能力培养、知识传授&ldquo;三位一体&rdquo;教育理念，完善全员、全过程、全方位的育人体系。健全学科分类建设机制，不断增强学术创新能力，加快哲学社会科学繁荣发展，在低维量子物理、结构生物学、密码学、类脑计算芯片、量子计算等领域取得一系列国际领先成果。改革社会服务体制机制，扎实做好对口支援和定点帮扶工作。制定实施全球战略，成立全球创新学院、中意设计创新基地、东南亚中心、拉美中心，创办苏世民书院、深圳国际研究生院，发起亚洲大学联盟、世界大学气候变化联盟、世界慕课联盟并担任主席单位，全球声誉显著提升。<strong>学校深入推进&ldquo;双一流&rdquo;建设，&ldquo;三个九年，分三步走&rdquo;总体战略目标如期实现，创建世界一流大学取得历史性跨越</strong>。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;在让我们刻骨铭心的2020年，清华大学始终把师生的生命安全和身体健康放在首位，坚持疫情防控不松懈、教学科研不停步、改革发展不放松，引领在线教育变革，全力推进抗疫科研攻关，加强抗疫国际交流合作，<strong>展现了面对危机和挑战时一所大学应有的责任担当，铸就了新时代教书育人的新气象</strong>。</p>\n<p><strong>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;大同爰跻，祖国以光。习近平总书记指出：&ldquo;办好高等教育，事关国家发展、事关民族未来</strong>。&rdquo;在奋力迈向世界一流大学前列的新阶段，清华大学将以习近平新时代中国特色社会主义思想为指导，牢牢把握学校改革发展的最好历史时期，坚持正确办学方向，落实立德树人根本任务，完善教育评价制度，打造一流创新体系，构建良好学术生态，全面深化改革，不断提升治理能力，努力实现新的引领，力争为国家发展和人类进步作出新的更大贡献。</p>\n<p class=\"vsbcontent_end\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&ldquo;一国之大学，当有其对於一国之任务；一代之大学，当有其处於一代之特点。&rdquo;<strong>自强成就卓越，创新塑造未来。在全面建设社会主义现代化国家的新征程上，自强的清华人永远以创新为矢志不渝的追求，永远保持奋进的姿态，努力开拓学校发展新格局，更自信、更从容地续写新的清韵华章，以优异成绩献礼建党100周年</strong>！</p>\n<p style=\"text-align: right;\">题图：霍元东</p>\n<p style=\"text-align: right;\">编辑：赵姝婧 陈晓艳</p>\n<p style=\"text-align: right;\">审核：吕婷</p>', NULL, '清华大学新闻网', '清华大学新闻网', 6, 0, NULL, 0, NULL, 0, 0, NULL, NULL, 1, '2021-04-13 18:59:26', '2021-04-13 18:59:26', '谢志朋、', '谢志朋、', 1, '2021-04-17 15:21:22', 1, '2021-04-17 15:21:22', 3, '2021-04-17 15:22:14', NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `news` VALUES (6, '滴滴出行支持武汉大学高水平人才培养启动仪式举行', '<p class=\"vsbcontent_start\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;新闻网讯（通讯员刘丹、舒悦）4月9日下午，滴滴出行支持武汉大学高水平人才培养启动仪式在我校举行。以此为契机，武汉大学和滴滴将在高水平人才培养、人才招聘、科研合作、课程建设等方面开启合作。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;副校长吴平，滴滴联合创始人兼CTO、滴滴自动驾驶公司CEO张博校友，滴滴出行方代表，我校相关职能部门及院系负责人，计算机学院师生代表出席了活动。</p>\n<p>武汉大学教育发展基金会副理事长兼秘书长邓小梅和滴滴出行科技生态与发展部总监吴国斌代表合作双方签署了相关协议。吴平向张博特别赠送了其在校期间的学生档案作为&ldquo;珞珈时光&rdquo;礼品。</p>\n<p class=\"vsbcontent_img\" style=\"text-align: center;\"><img class=\"img_vsb_content\" title=\"\" src=\"https://news.whu.edu.cn/__local/F/0B/A8/60E11662DAEDF120EB54C6C73A0_C6403637_1A65A.png\" width=\"600\" /></p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;张博对母校长期以来对滴滴的支持表示感谢，希望能与母校在共享出行核心关键技术、自动驾驶、汽车创新研发、智能出行人工智能平台等方面共同探索前沿，打造人才培养新机制，在人才、技术和场景等多方面优势互补，为社会发展做出贡献。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;吴平指出，滴滴出行是国家科技创新的代表性企业之一，在智慧交通、机器学习、数据挖掘和大数据等领域，取得了多项业界领先的技术成果，成绩斐然。双方开启人才联合培养，契合学校人才培养的需要。武汉大学将以此为契机，定向培养一批应用型、创新型人才，使学校的人才培养与社会接轨、与行业需求接轨，符合发展趋势，满足社会需求。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;张博为在场400余位武大学子带来题为&ldquo;用科技D造未来&rdquo;的主题演讲，并现场发布滴滴未来精英人才品牌，特别介绍了滴滴近期开启的未来精英实习生计划和博士后工作站，邀请包括武汉大学在内的全球高校科技人才成为滴滴未来精英，一同在多元化的业务场景里，攻克核心技术难题和探索技术前沿，让世界变得更加美好。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;返校期间，张博还拜访了校长窦贤康院士、副校长李建成院士，在计算机学院召开了&ldquo;滴滴未来精英&middot;校园行&rdquo;精英人才圆桌论坛。张博是我校国际软件学院2005届软件工程本科毕业生。</p>\n<p class=\"vsbcontent_end\" style=\"text-align: right;\">（编辑：肖珊）</p>', NULL, '武大新闻网', '武大新闻网', 3, 0, NULL, 0, NULL, 0, 0, NULL, NULL, 1, '2021-04-13 20:32:44', '2021-04-13 20:39:55', '谢志朋、', NULL, 1, '2021-04-16 21:09:02', 1, '2021-04-16 21:09:02', 1, NULL, NULL, NULL, NULL, 0, 0, '{\"REVIEW_FAIL\":{\"epoch\":1,\"reviewTime\":\"2021-04-16 21:10\",\"reviewer\":{\"id\":1},\"suggestion\":\"测试一审审核失败\"}}');
INSERT INTO `news` VALUES (25, '湖南省政协副主席赖明勇来校调研人才工作', '<div class=\"subCont\">\n<div class=\"v_news_content\">\n<p class=\"vsbcontent_start\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;本网讯 &nbsp;4月8日下午，湖南省政协副主席赖明勇率队来校调研人才工作。校党委书记易红、副书记李亮参加调研。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;当日下午，赖明勇一行参观了高性能复杂制造国家重点实验室、国家重金属污染防治工程技术研究中心。在科教楼三会议室，与学校相关单位代表座谈，重点围绕&ldquo;进一步完善引进和留住人才的激励政策，为实施&lsquo;三高四新&rsquo;战略提供人才支撑&rdquo;进行讨论。</p>\n<p class=\"vsbcontent_img\"><img class=\"img_vsb_content\" style=\"display: block; margin-left: auto; margin-right: auto;\" title=\"\" src=\"http://news.csu.edu.cn/__local/9/D7/87/3AEDBF5F01EC5FA7C9D03D88E27_BB6AB14D_251C14.jpeg\" alt=\"\" width=\"600\" height=\"400\" border=\"0\" hspace=\"0\" vspace=\"0\" data-bd-imgshare-binded=\"1\" /></p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;近年来，中南大学在人才队伍建设方面注重谋篇布局，专门成立人才工作领导小组，加强顶层设计，健全人才引育体系；注重全球延揽，拓宽引才视野，创新工作思路，实施柔性引智计划；注重靶向培育，坚持引育并举，设立战略先导专项，关注青年人才成长；注重精细服务，建立党委联系服务专家制度，推行一站式服务，深化&ldquo;放管服&rdquo;改革，建立人才择优选聘机制，使得人才优先发展、协同发展、提速发展、潜心发展。</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;赖明勇指出，创新是第一动力，人才是第一资源。充分利用好高校人才资源是做好湖南省人才建设工作的重要组成。中南大学是湖南的科技创新高地和人才聚集高地，在引才、育才以及稳定人才方面的政策、举措、制度及建设性意见，为全省人才队伍建设工作提供了可靠数据和参考意见。希望学校进一步探索和提供好的经验做法，加大人才队伍建设力度，为实施&ldquo;三高四新&rdquo;战略提供更大的人才和智力支撑。</p>\n<p class=\"vsbcontent_end\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;湖南省政协相关单位负责人，学校党委组织部、统战部、人事处、科研部、研究生院、物电院、材料院及湘雅医院等单位代表参加座谈。</p>\n</div>\n</div>\n<p>&nbsp;</p>', 13, '中南大学新闻网', '中南大学新闻网', 5, 1, '2021-04-19 21:04:49', 1, '2021-04-19 21:05:57', 0, 0, NULL, NULL, 1, '2021-04-13 21:08:47', '2021-04-13 21:08:47', '谢志朋、王五、钱六六、赵七、', '谢志朋、', 1, '2021-04-17 15:27:31', 1, '2021-04-17 15:27:31', 3, '2021-04-17 15:27:49', 1, '2021-04-19 10:11:06', '2021-04-19 10:10:00', 1000, 0, '{\"CAROUSEL_IMAGE_URL\":\"http://news.csu.edu.cn/__local/9/D7/87/3AEDBF5F01EC5FA7C9D03D88E27_BB6AB14D_251C14.jpeg\"}');
INSERT INTO `news` VALUES (38, '【聚焦就业】落实改革见行动，就业工作呈新貌', '<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;2021年，学校党委启动了就业工作综合改革，下达了打好就业工作翻身仗的动员令。全校各单位将就业工作与开展党史教育活动相结合，积极行动，协同奋进，密切配合，就业工作稳步推进，呈现出崭新面貌。</p>\n<p style=\"text-align: center;\"><strong>下沉基层，就业工作更深入</strong></p>\n<p>&nbsp;</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.jxufe.edu.cn/uploadfile/82/Attachment/ba9f0e565c.png\" data-layer=\"photo\" data-bd-imgshare-binded=\"1\" /></p>\n<p>　　4月9日，副校长欧阳康带队先后到法学院、人文学院、会计学院调研毕业生就业工作。每到一处，欧阳康都详细询问了毕业生就业动态数据、就业工作举措、就业工作中存在的难点和问题，听取了各个单位的建议，提出了工作要求。欧阳康表示，今后还将继续深入各单位了解就业工作情况，与各单位群策群力，共同讨论就业难题的破解之道。</p>\n<p>　　此前，招生就业处处长柳晨带队先后到会计学院、金融学院、财税与公共管理学院、马克思主义学院等单位调研就业工作，与各单位共同商议如何推进就业工作改革。</p>\n<p style=\"text-align: center;\"><strong>明确责任，就业工作更务实</strong></p>\n<p>　　各单位积极响应校党委号召，进一步统一思想认识，提高政治站位，牢固树立就业工作全员化理念，明确职责分工，将就业工作责任落到实处。各单位党政一把手齐抓共管，对就业工作同部署、同调度、同推进。如会计学院进一步压实研究生就业责任，将研究生导师和研究生班主任与研究生一一对应，落实细化就业工作职责。</p>\n<p style=\"text-align: center;\"><strong>建立台账，就业工作更细致</strong></p>\n<p>　　学校《就业工作综合改革方案》印发之后，招生就业处立即将本单位承担的改革任务进行分工，并将责任落实到人，建立就业工作台账，实施任务进展每日一报制度。各学院都根据本院毕业生实际情况，建立就业工作台账，将工作任务明确到个人。如马克思主义学院实施挂图作战，就业一人销账一个，实行就业工作的&ldquo;一人一号&rdquo;&ldquo;一人一策&rdquo;举措。</p>\n<p style=\"text-align: center;\"><strong>走进学生，就业工作更精准</strong></p>\n<p>　　财税与公共管理学院、会计学院、法学院、人文学院等深入了解考研学生的成绩及上线情况，针对报考不同院校的学生分别提供专人指导服务，针对参加复试、申请调剂和未上线的毕业生精准施策，做好指导、服务和引导工作。招生就业处深入了解报考公职岗位的培训需求，通过与第三方机构合作，为有需求的毕业生提供考前冲刺培训。各学院针对身体条件好、国防意识强的毕业生，积极做好征兵宣传工作，讲清最新政策，鼓励毕业生参军入伍。</p>\n<p style=\"text-align: center;\"><strong>开</strong><strong>拓市场，就业工作更主动</strong></p>\n<p>　　招生就业处广开门路，积极开拓省内省外两个市场，联系用人单位来校招聘，截止3月底，已为2021届毕业生引进就业岗位7万余个。各培养单位主动作为，广泛联系用人单位来校来院招聘。旅游与城市管理学院院领导带队赴上海等地开拓就业市场，软件与物联网工程学院积极邀请用人单位来校举行院级双选会，工商管理学院、会计学院、统计学院等正筹划举办中小型双选会，进一步丰富岗位供给。（文/招生就业处&nbsp;&nbsp;&nbsp; 编辑/姜莹&nbsp; 周雨菲）</p>', NULL, '江西财经大学新闻网', '江西财经大学新闻网', 0, 0, NULL, 0, NULL, 0, 0, NULL, NULL, 1, '2021-04-13 23:49:11', NULL, NULL, NULL, NULL, NULL, NULL, '2021-04-13 23:52:10', 0, NULL, NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `news` VALUES (40, '弘扬英雄精神 黄群塑像在校揭幕', '<p class=\"vsbcontent_start\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;新闻网讯（记者 张思晗）4月9日，黄群塑像揭幕仪式在船海学院东二楼庭院举行。校长李元元、黄群校友夫人亢群共同为黄群塑像揭幕。&ldquo;黄群班&rdquo;学生代表为塑像敬献鲜花。</p>\n<p class=\"vsbcontent_start\">&nbsp;</p>\n<p class=\"vsbcontent_img\" style=\"text-align: center;\"><img class=\"img_vsb_content\" src=\"http://news.hust.edu.cn/__local/8/38/39/30980BF057D3C55397BC7783263_7D02D63E_17E15.jpg\" width=\"500\" data-bd-imgshare-binded=\"1\" /></p>\n<p class=\"vsbcontent_img\">&nbsp;</p>\n<p class=\"vsbcontent_img\" style=\"text-align: center;\"><img class=\"img_vsb_content\" src=\"http://news.hust.edu.cn/__local/8/A4/EE/8E315CD308496547DD085D72045_194C37BC_177FB.jpg\" width=\"500\" data-bd-imgshare-binded=\"1\" /></p>\n<p class=\"vsbcontent_img\">&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;2018年8月20日，黄群和队友因在台风中抢救国家重点试验平台壮烈牺牲。习近平总书记作出重要批示，黄群等3位同志用实际行动诠释了共产党员对党忠诚、恪尽职守、不怕牺牲的优秀品格，用宝贵生命践行了共产党员&ldquo;随时准备为党和人民牺牲一切&rdquo;的初心和誓言，他们是共产党员的优秀代表、时代楷模。此次落成的黄群塑像材质由铜、锈蚀钢、不锈钢组成，人像有70公分，基座140公分；内部为凹凸不平的锈蚀钢构成，高低搭配组合，肌理参照码头防护墙。外部为拉丝不锈钢，以浪花为参照蓝本。</p>\n<p>&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;李元元代表学校向捐资筹建黄群塑像的校友师生表示感谢。他指出，习近平总书记充分肯定了黄群同志的先进事迹，作为黄群同志的母校，我们有责任继承好黄群同志的精神，薪火相传、代代承续。李元元强调，当前学校正在掀起党史学习教育热潮，全校师生要进一步深入学习、宣传黄群同志的先进事迹，将其转化为思想教育优质教材；要进一步讲好新时代华中大故事，用身边人说身边事，用身边事带动身边人；要进一步凝练&ldquo;黄群班&rdquo;建设经验，将&ldquo;黄群精神&rdquo;融入立德树人工作；要进一步提振干事创业精气神，脚踏实地、埋头苦干，确保&ldquo;十四五&rdquo;开好局、起好步，为中国共产党成立100周年献礼。</p>\n<p>&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;亢群表示，黄群是亲人、是战友、是挚爱。黄群从船海学院启航，在中国共产党的领导下，在对理想信念的不断追求下，逐渐成长为一名目标明确、技术过硬、管理一流的优秀共产党员，一名为事业献出宝贵生命的时代楷模。她希望黄群同志将入党誓词贯穿于生命、将强军梦想牢记于心中、将宝贵生命献给钟爱事业的英雄壮举，滋养同学们的心灵，激励同学们树立正确的人生观和价值观。</p>\n<p>&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;船海学院院长解德介绍了黄群同志的生平事迹和&ldquo;黄群班&rdquo;建设情况。他指出，黄群同志是我校众多杰出校友中的光荣典范，其身上体现出的&ldquo;老黄牛&rdquo;精神，充分展现了华中大优秀学子的精神风貌。大家要以黄群同志为榜样，不忘初心，牢记使命，坚持立德树人根本任务，为建设海洋强国培养出更多的卓越人才。</p>\n<p>&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;中国船舶集团第760研究所纪委书记贾占滨介绍了黄群同志所负责试验平台的建设情况，希望同学们以英烈为榜样，在校园里孜孜以求、发奋图强，为中国梦强国梦的实现贡献力量。</p>\n<p>&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;首届&ldquo;黄群班&rdquo;班党支部书记刘娇汇报了班级建设情况，表示将坚持学习、传承、践行&ldquo;黄群精神&rdquo;，勤学笃行、矢志报国，做德智体美劳全面发展的合格建设者和可靠接班人。</p>\n<p>&nbsp;</p>\n<p class=\"vsbcontent_img\"><img class=\"img_vsb_content\" style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.hust.edu.cn/__local/7/AC/2E/70A50607D0F7947EFA87DE78645_B689FA95_18882.jpg\" alt=\"18882\" width=\"500\" data-bd-imgshare-binded=\"1\" /></p>\n<p class=\"vsbcontent_img\">&nbsp;</p>\n<p style=\"text-align: center;\">记者 刘涵木 摄</p>\n<p>&nbsp;</p>\n<p class=\"vsbcontent_end\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;学校相关部门负责人，船海学院党政班子成员，中国船舶集团第760研究所、第719研究所代表，以及师生代表等参加活动。</p>', NULL, '华中科技大学新闻网', '华中科技大学新闻网', 0, 0, NULL, 0, NULL, 0, 0, NULL, NULL, 1, '2021-04-14 10:43:58', NULL, NULL, NULL, NULL, NULL, NULL, '2021-04-14 10:46:12', 0, NULL, NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `news` VALUES (42, '公卫学院携手硚口科协助力社区健康科普', '<p class=\"vsbcontent_start\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;新闻网讯（通讯员 邸红昆）4月9日上午，结合湖北省营养协会举办的&ldquo;全民营养周&rdquo;契机，公卫学院携手武汉市硚口区科协，举行了今年第二场&ldquo;健康新时代、青春公卫行&rdquo;活动，助力硚口集贤里社区健康科普。此次活动以&ldquo;老年人营养与健康&rdquo;为主题，学院30余名本硕博师生和党员骨干组成志愿服务团，走进集贤里社区，为社区内的老年居民进行健康知识科普宣教服务。公卫学院党委副书记冯霞、硚口区科协黄来生副主席、郑春阳主任参与活动。</p>\n<p class=\"vsbcontent_start\">&nbsp;</p>\n<p class=\"vsbcontent_img\"><img class=\"img_vsb_content\" style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.hust.edu.cn/__local/7/17/A0/D7D26EEDB4ABA2EE933C9AD397C_3C9AC036_1C81B.jpg\" width=\"500\" data-bd-imgshare-binded=\"1\" /></p>\n<p class=\"vsbcontent_img\">&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;上午9：00，湖北省营养学会常务理事、公卫学院营养与食品卫生学系的郝丽萍教授，声情并茂地为在场的居民进行了一场深入浅出的营养健康知识宣讲。郝丽萍首先提到我国于2019年出台的《健康中国行动（2019&mdash;2030年）》，围绕疾病预防和健康促进两大核心提出15个重大专项行动，促进以&ldquo;治病&rdquo;为中心向以&ldquo;健康&rdquo;为中心转变，努力使群众不生病、少生病。她围绕合理膳食和老年健康促进两大专项行动，分别向居民介绍了人体营养需要、中国居民膳食指南和实践应用、以及老年人生理代谢特点和营养需求。郝丽萍指出，随着年龄的增长，老年人新陈代谢降低，对营养的需求增加了，对能量的需求降低了，每日膳食应遵循《中国老年人膳食指南》，努力做到合理膳食、均衡营养，减少和延缓疾病的发生发展。</p>\n<p>&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;宣讲内容贴近实际、贴近生活、贴近群众，讲透了观点，讲清了重点，具有很强的吸引力和感染力。宣讲结束后，反响热烈，郝丽萍就武汉传统早餐&ldquo;热干面&rdquo;和植物雌激素等保健品适不适合老年人每日食用等问题与在场的居民进行了深切的交流。郝丽萍提出适合老年人特点的膳食指导，才能更好适应身体功能改变。</p>\n<p>&nbsp;</p>\n<p class=\"vsbcontent_img\"><img class=\"img_vsb_content\" style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.hust.edu.cn/__local/7/0B/61/277554E2D8893F161D5222E603A_5E36DF4D_27C1B.jpg\" width=\"500\" data-bd-imgshare-binded=\"1\" /></p>\n<p class=\"vsbcontent_img\">&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;为做到点对点精准宣传，让健康知识落地生根、入脑入心。志愿服务团的十余名党员志愿者们到社区入户开展宣传工作，分发《预防慢性病（糖尿病、高血压）》和《保健食品小知识》等资料，将慢病防治知识以及健康常识传达至各户居民，并发放口罩和洗手凝胶等公共卫生物品，教育引导居民提升疫情防护意识。</p>\n<p class=\"vsbcontent_img\">&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;随后，我院党员骨干为在场的社区居民进行肩颈操和七步洗手法的现场教学。现场居民和志愿者们一同学习效仿，身心得到了放松，现场气氛达到了高潮。</p>\n<p>&nbsp;</p>\n<p><img class=\"img_vsb_content\" style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.hust.edu.cn/__local/D/73/CC/EE789E20F26985FA8AE326E38AB_D9A75C36_1F873.jpg\" width=\"500\" data-bd-imgshare-binded=\"1\" /></p>\n<p>&nbsp;</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;身穿白大褂的志愿者们为社区居民提供血压测量和健康咨询服务，他们耐心询问了居民们的日常生活行为方式，并对居民们提出的自身健康问题进行了积极的解答，同时纠正了居民们一些错误的健康观和养生观。</p>\n<p>&nbsp;</p>\n<p class=\"vsbcontent_img\"><img class=\"img_vsb_content\" style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.hust.edu.cn/__local/5/CC/3A/24311FB57A6E0951B3468D3B20F_E696B2B5_23AB7.jpg\" width=\"500\" data-bd-imgshare-binded=\"1\" /></p>\n<p class=\"vsbcontent_img\">&nbsp;</p>\n<p style=\"text-align: center;\">通讯员 骆曜宇 摄</p>\n<p>&nbsp;</p>\n<p class=\"vsbcontent_end\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;本次活动得到了硚口区科协和社区居民的认可，居民们表示，这样的讲座很接地气、很受用，让我们老年居民对自己和家人的健康和防疫知识有了科学的认识。参加此次活动的公卫学子们，通过这样的科普健康社会实践活动更加深刻认识到自己应该承担的时代责任。</p>', NULL, '华中科技大学新闻网', '华中科技大学新闻网', 7, 0, NULL, 0, NULL, 0, 0, NULL, NULL, 1, '2021-04-14 12:29:58', '2021-04-14 21:27:40', '谢志朋、王五、', NULL, NULL, NULL, 1, '2021-04-16 21:07:11', 0, NULL, NULL, NULL, NULL, 0, 0, '{\"RE_MODIFICATION\":{\"operateTime\":\"2021-04-16 21:55\",\"operator\":{\"id\":1},\"suggestion\":\"系统温馨提示：系统的审核流程发生变化，所有审核中的新闻全部回到中转站！\"}}');
INSERT INTO `news` VALUES (67, '【迎百年 学党史】校妇委会开展喜迎建党100周年东湖绿道健步行活动', NULL, NULL, NULL, NULL, 0, 0, NULL, 0, NULL, 0, 0, NULL, 'http://news.hust.edu.cn/info/1002/41536.htm', 1, '2021-04-15 20:06:29', NULL, NULL, NULL, NULL, NULL, NULL, '2021-04-15 20:06:30', 0, NULL, NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `news` VALUES (68, '竹外桃花三两枝 春江水暖鸭先知', '<p style=\"text-align: left;\">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;经历北方漫长的冬季，春姑娘翩然而至。3月的燕园春和景明、水波荡漾，扑面而来的温柔煦风，浸润着春日的美好气息。春分前后的细密酥雨，滋润满园芳菲，催绿枝头青芽，更让园子里的小动物们&ldquo;抖擞&rdquo;了精神。竹外桃花三两枝，静享闲然悠趣，若不想辜负大美春光，请跟随我们的相机，记录下这满园生趣。</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-03/18b06166d596494ca4142f02c8eebab1.jpeg\" width=\"554\" height=\"312\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-03/da0b6fbbfa5b47c79cbdac0fd0b9d86d.jpeg\" width=\"554\" height=\"312\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-03/efc4fd4b2b2f43459a4da810e4c45a09.jpeg\" width=\"552\" height=\"311\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-03/62d68245d41c4dc4b8109b4e5333dcf8.jpeg\" width=\"554\" height=\"312\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-03/c211b3b96fa541908b781370ab605ab1.jpeg\" width=\"554\" height=\"312\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-03/b3c050212bff42048d02d72c77706e80.jpeg\" width=\"554\" height=\"312\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-03/0d2294195ff6401ba632ba4200d90a49.jpeg\" width=\"554\" height=\"312\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img title=\"ee5e02717cef47a099530567f9e984c1.jpg\" src=\"http://news.pku.edu.cn/images/2021-03/ee5e02717cef47a099530567f9e984c1.jpg\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img title=\"16024f7b96c9405b8df3c640c8338d09.jpg\" src=\"http://news.pku.edu.cn/images/2021-03/16024f7b96c9405b8df3c640c8338d09.jpg\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img title=\"bd22368c3cf34e8cbcc4a6a911e512af.jpg\" src=\"http://news.pku.edu.cn/images/2021-03/bd22368c3cf34e8cbcc4a6a911e512af.jpg\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img title=\"beaf3a02063347aab78f1fc0f7bb7056.jpg\" src=\"http://news.pku.edu.cn/images/2021-03/beaf3a02063347aab78f1fc0f7bb7056.jpg\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img title=\"2b1550b7473e4a93a28898ab0bbd680e.jpg\" src=\"http://news.pku.edu.cn/images/2021-03/2b1550b7473e4a93a28898ab0bbd680e.jpg\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img title=\"a78b5c99dea84c29bd10d41e91468f68.jpg\" src=\"http://news.pku.edu.cn/images/2021-03/a78b5c99dea84c29bd10d41e91468f68.jpg\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-03/c43bdf81c11a4dfebaab0c81de207b61.jpeg\" width=\"554\" height=\"312\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img title=\"3226bd7c1ece4652953f6acf81d4bdda.jpg\" src=\"http://news.pku.edu.cn/images/2021-03/3226bd7c1ece4652953f6acf81d4bdda.jpg\" alt=\"df3897e52c69f2c4670d4744ac917ef_调整大小.jpg\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\"><img title=\"\" src=\"http://news.pku.edu.cn/images/2021-03/8a83616b71944c67ae89588ee9c87c94.jpg\" alt=\"微信图片_20210323172133.jpg\" /></p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\">&nbsp;</p>\n<p style=\"text-align: center;\">&nbsp;</p>', 6, '北京大学新闻网', '北京大学新闻网', 5, 1, '2021-04-19 21:04:07', 0, NULL, 1, 1, '2021-04-19 21:03:34', NULL, 1, '2021-04-16 17:58:30', '2021-04-16 17:58:31', '谢志朋、', '谢志朋、', 1, '2021-04-18 16:16:38', 1, '2021-04-18 16:16:38', 3, '2021-04-18 16:16:56', 1, '2021-04-19 11:50:52', '2021-04-19 11:50:00', 0, 0, '{\"CAROUSEL_IMAGE_URL\":\"http://news.pku.edu.cn/images/2021-03/efc4fd4b2b2f43459a4da810e4c45a09.jpeg\"}');
INSERT INTO `news` VALUES (69, '“庆百年荣光 谱青春芳华”——光华管理学院首届“光华杯”迎春跑活动举行', '<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;4月10日，由光华管理学院工会和团委共同举办的首届&ldquo;光华杯&rdquo;迎春跑活动举行，活动以&ldquo;庆百年荣光，谱青春芳华&rdquo;为主题，将党史学习教育融入到春季户外跑步当中，号召全院师生以协力奔跑的形式献礼建党百年。在2021年的第100天，校内校外齐联动，天南地北共相约。光华管理学院副院长吴联生、张圣平、马力，党委副书记滕飞、团委书记鞠晓等50余名教职工通过校内、校外跑形式参与到活动中，近千名师生一起奔跑在神州大地的春光里。</p>\n<p style=\"text-align: center;\"><img src=\"http://news.pku.edu.cn/images/2021-04/1128db6f2efa4afd9074eab8eff31bb5.jpeg\" width=\"554\" height=\"370\" /></p>\n<p style=\"text-align: center;\">出发前的活动现场</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;迎春跑的校内活动起点设置在光华管理学院1号楼西侧&ldquo;敢当石&rdquo;前。上午8:00开始，陆续有师生来到此处报到、热身，随即开始3km的奔跑活动，更有老师和同学以党团支部、课题组、系所、班级、宿舍等为单位共同起跑，凝聚基层组织活力，共同庆祝建党百年。</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.pku.edu.cn/images/2021-04/eb4f6ae475764f4792a8e371ed26ded4.jpeg\" width=\"454\" height=\"303\" /></p>\n<p>&nbsp;</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.pku.edu.cn/images/2021-04/04bd853805e74b96b2cc1ff4c3695fae.jpeg\" width=\"454\" height=\"303\" /></p>\n<p style=\"text-align: center;\">跑前共同合影</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&ldquo;重温党史&rdquo;主题学习展板依次放置于更衣区外的大厅走廊上。峥嵘岁月，逐梦前行。从党的一大召开到遵义会议确立毛泽东同志在党和红军中的领导地位，从中华人民共和国成立到改革开放，从中国加入世贸组织到全面建成小康社会，百年征程波澜壮阔，百年初心历久弥坚，参加活动的师生纷纷驻足，一起重温百年党史，不忘初心，牢记使命，奋进新时代，作出新贡献。</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.pku.edu.cn/images/2021-04/07cf784499544453954c46ddae6025ca.jpeg\" width=\"554\" height=\"280\" /></p>\n<p style=\"text-align: center;\">&ldquo;重温党史&rdquo;展板</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;校内活动的终点设在光华新楼，在终点，精心设计和准备的纪念T恤、奖牌、合影、盖有特制印章的纪念册让每一名参与者感受到了满满的成就感与获得感。师生们纷纷为迎春跑活动点赞&mdash;&mdash;&ldquo;在跑步过程中发现了许多未曾留意过的燕园美景，感受到了运动带来的释放和快意&rdquo;&ldquo;强身健体的同时和这么多光华人一起为建党100周年献礼，非常有意义&rdquo;&ldquo;参加迎春跑，是为了锻炼身体、丰富生活，更好地为祖国工作，这次活动参与的人很多，大家一起跑很有团体归属感。&rdquo;</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.pku.edu.cn/images/2021-04/f8d0c42a988a49f380f03db4644ee1af.jpeg\" width=\"554\" height=\"370\" /></p>\n<p>&nbsp;</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.pku.edu.cn/images/2021-04/9263b75f1f8b4896b628bab0a03f7dbb.jpeg\" width=\"554\" height=\"370\" /></p>\n<p style=\"text-align: center;\">师生跑步</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;校外活动部分与校内活动同步开始，4月10日至12日，在全国各地近30个城市，近400名师生参与了校外的&ldquo;云跑步&rdquo;，光华管理学院深圳分院、成都分院的师生们还自发组织了集体起跑。燕园内外，身处祖国各地的光华学子都用飞扬的脚步和蓬勃的朝气迎接春天的到来。</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.pku.edu.cn/images/2021-04/4aace09ccaa34f4592da380cb67e5b62.png\" width=\"454\" height=\"286\" /></p>\n<p style=\"text-align: center;\">校外跑活动：深圳分院班级同学同步开跑</p>\n<p><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"http://news.pku.edu.cn/images/2021-04/0de304ab7bfc40e093ba73c07840734f.jpeg\" width=\"554\" height=\"370\" /></p>\n<p style=\"text-align: center;\">完成跑步后合影</p>\n<p>&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;春光正好，万物可亲。翰墨与弦歌之外，课堂与作业间隙，光华师生们奔跑于燕园春色之中，歌颂青春，献礼祖国。融党史学习于健身活动，为体育育人赋思想内涵，无论是昂扬奋进的参赛者，还是加油呐喊的志愿者，抑或是默默奉献的组织者，都在活动参与之中感受到了这次迎春跑的非凡意义。光华师生将进一步凝聚共识，砥砺奋斗，把党史学习教育同学院发展实际结合起来，以昂扬姿态奋力开启全面建设社会主义现代化国家新征程，以优异成绩迎接建党一百周年。</p>\n<p>&nbsp;</p>\n<p><a href=\"http://news.pku.edu.cn/ztrd/dsxxjy/index.htm\" target=\"%22%2522%252522%25252522_blank%25252522%252522%2522%22\"><img src=\"http://news.pku.edu.cn/images/2021-04/28d82a2af69f4a44836908d80937d86f.jpeg\" width=\"536\" height=\"58\" /></a></p>', NULL, '北京大学新闻网', '北京大学新闻网', 7, 0, NULL, 0, NULL, 0, 0, NULL, NULL, 1, '2021-04-16 19:26:12', '2021-04-16 19:26:13', '谢志朋、钱六六、', NULL, NULL, NULL, 1, '2021-04-17 15:26:48', 0, NULL, NULL, NULL, NULL, 0, 0, '{\"RE_MODIFICATION\":{\"operateTime\":\"2021-04-19 09:58\",\"operator\":{\"id\":1},\"suggestion\":\"系统温馨提示：系统的审核流程发生变化，所有审核中的新闻全部回到中转站！\"}}');
INSERT INTO `news` VALUES (72, '深入推进“人才战略年”的各项工作——北京大学召开院长（系主任）行政月度会', NULL, 8, NULL, NULL, 5, 0, NULL, 0, NULL, 0, 1, '2021-04-19 21:03:13', 'http://news.pku.edu.cn/xwzh/3f1fb4b67c43428dba688ea34891484b.htm', 1, '2021-04-19 09:56:33', '2021-04-19 09:56:35', '谢志朋、', NULL, 1, '2021-04-19 10:06:10', 1, '2021-04-19 10:06:10', 1, '2021-04-19 10:06:10', 1, '2021-04-19 17:19:43', '2021-04-19 17:19:00', 0, 0, NULL);

-- ----------------------------
-- Table structure for news_column
-- ----------------------------
DROP TABLE IF EXISTS `news_column`;
CREATE TABLE `news_column`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '新闻栏目名称',
  `description` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对栏目的描述',
  `menu_order` tinyint(0) NOT NULL DEFAULT 127 COMMENT '菜单显示顺序，默认有个最大值，用于排序',
  `module_order` tinyint(0) NULL DEFAULT NULL COMMENT '模块序号，决定在首页哪个模块上显示该栏目新闻列表，为null则不在首页上显示新闻列表',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父栏目id',
  `enabled` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否开启该栏目;0-false,1-true',
  `external_link` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目链接。如果新闻栏目是其他网站，则可以设置栏目链接用于跳转',
  `is_has_children` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否有子栏目',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_title`(`title`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新闻栏目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news_column
-- ----------------------------
INSERT INTO `news_column` VALUES (1, '要闻聚焦', NULL, 1, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', '2021-04-19 15:42:50');
INSERT INTO `news_column` VALUES (2, '信息公告', NULL, 2, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', '2021-04-19 15:43:00');
INSERT INTO `news_column` VALUES (3, '教学资讯', NULL, 3, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', '2021-04-19 15:42:51');
INSERT INTO `news_column` VALUES (4, '科研动态', NULL, 4, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', '2021-04-19 15:43:01');
INSERT INTO `news_column` VALUES (5, '学生天地', NULL, 5, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column` VALUES (6, '缤纷校园', NULL, 6, NULL, NULL, 1, NULL, 1, '2021-04-09 15:17:58', '2021-04-10 14:25:06');
INSERT INTO `news_column` VALUES (7, '专题报道', NULL, 7, NULL, NULL, 1, NULL, 1, '2021-04-09 15:17:58', '2021-04-09 15:49:17');
INSERT INTO `news_column` VALUES (8, '媒体校园', NULL, 8, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column` VALUES (9, '交流合作', NULL, 9, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column` VALUES (10, '人物风采', NULL, 10, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column` VALUES (11, '校友动态', NULL, 11, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column` VALUES (12, '校史钩沉', NULL, 12, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column` VALUES (13, '众志成城 抗击疫情', NULL, 1, NULL, 7, 1, NULL, 0, '2021-04-09 15:37:06', '2021-04-09 23:20:18');
INSERT INTO `news_column` VALUES (14, '不忘初心 牢记使命', NULL, 2, NULL, 7, 1, NULL, 0, '2021-04-09 15:37:06', '2021-04-09 23:20:21');
INSERT INTO `news_column` VALUES (15, '文明校园建设', NULL, 3, NULL, 7, 1, NULL, 0, '2021-04-09 15:37:06', '2021-04-09 23:20:19');
INSERT INTO `news_column` VALUES (16, '40周年专题', NULL, 4, NULL, 7, 1, 'http://news.jxufe.edu.cn/news-list-40znzt.html', 0, '2021-04-09 15:37:06', '2021-04-10 00:08:53');
INSERT INTO `news_column` VALUES (17, '校园光影', '记录校园的美好景色', 1, NULL, 6, 1, NULL, 0, '2021-04-10 11:41:15', '2021-04-19 15:43:14');

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url_pattern，也就是权限管理的对象（资源）；如果菜单不绑定资源，就为null',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑定的菜单名（因为资源和菜单一对一绑定了）',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑定的菜单对应的路由路径',
  `component` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑定的菜单对应的组件',
  `icon_cls` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标class',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父菜单id',
  `enabled` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否启用菜单',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源描述',
  `sub_menu` tinyint(0) NULL DEFAULT 0 COMMENT '是否是子菜单，该字段作为路由对象的meta信息。该字段是为前端面包屑功能服务的，对于子菜单，面包屑上不允许点击跳转',
  `hidden` tinyint(0) NOT NULL DEFAULT 0 COMMENT '1:则该路由不作为侧边栏菜单；0:表示该路由将作为侧边栏菜单显示',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_unique`(`name`) USING BTREE,
  UNIQUE INDEX `url_unique`(`url`) USING BTREE,
  INDEX `parent_id_index`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源和路由的合并表，菜单根据路由表生成（由于很多路由的目标组件上都会绑定一些后端的接口资源，所以进行了合并）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (1, NULL, '系统管理', '/', 'Layout', NULL, NULL, 1, NULL, 1, 0);
INSERT INTO `resource` VALUES (2, '/management/column/**', '新闻栏目管理', '/newsCol', 'NewsCol', NULL, NULL, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (3, '/management/news/public/**', '新闻管理', '/', 'Layout', NULL, NULL, 1, '新闻管理的公共资源', 1, 0);
INSERT INTO `resource` VALUES (4, '/management/notice/**', '公告管理', '/announcement', 'Notice', NULL, NULL, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (5, '/management/link/**', '友情链接管理', '/links', 'FriendshipLink', NULL, NULL, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (6, '/management/user/**', '用户管理', '/system/user', 'User', NULL, 1, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (7, '/management/role/**', '角色管理', '/system/privilege', 'Privilege', NULL, 1, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (8, '/management/log/news/**', '新闻操作日志', '/system/log', 'Log', NULL, 1, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (9, '/management/system/config/**', '系统设置', '/system/config', 'Config', NULL, 1, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (10, '/management/news/upload/**', '撰写新闻', '/news/edit', 'NewsEdit', NULL, 3, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (11, '/management/news/transit/**', '新闻中转站', '/news/transit', 'NewsTransit', NULL, 3, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (12, NULL, '新闻审核', '#review', 'ReviewRouterView', NULL, 3, 1, NULL, 1, 0);
INSERT INTO `resource` VALUES (13, '/management/news/review/1/**', '一审', '/news/review/1', 'NewsReview', NULL, 12, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (14, '/management/news/review/2/**', '二审', '/news/review/2', 'NewsReview', NULL, 12, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (15, '/management/news/review/3/**', '三审', '/news/review/3', 'NewsReview', NULL, 12, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (16, '/management/news/pub/**', '新闻发布', '/news/publish', 'NewsPub', NULL, 3, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (17, '/management/news/status/**', '新闻状态追踪', '/news/status', 'NewsStatus', NULL, 3, 1, NULL, 0, 0);
INSERT INTO `resource` VALUES (21, '/management/news/review/4/**', '四审', '/news/review/4', 'NewsReview', NULL, 12, 0, NULL, 0, 0);
INSERT INTO `resource` VALUES (22, '/management/news/review/5/**', '五审', '/news/review/5', 'NewsReview', NULL, 12, 0, NULL, 0, 0);
INSERT INTO `resource` VALUES (23, '/management/news/review/6/**', '六审', '/news/review/6', 'NewsReview', NULL, 12, 0, NULL, 0, 0);
INSERT INTO `resource` VALUES (24, NULL, '下拉菜单', '/', 'Layout', NULL, NULL, 1, '仅作为路由，不绑定url资源。该路由是下拉菜单的父路由，匹配Layout布局组件，不作为侧边栏菜单显示', 1, 1);
INSERT INTO `resource` VALUES (25, NULL, '草稿箱', '/dropDownMenu/newsDrafts', 'NewsDrafts', NULL, 24, 1, '下拉菜单的草稿箱路由，不作为侧边栏菜单显示', 0, 1);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name_en` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色英文名',
  `name_zh` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色中文名',
  `description` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `is_system_admin` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否是系统管理员',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_zh_unique`(`name_zh`) USING BTREE,
  UNIQUE INDEX `name_en_unique`(`name_en`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 123 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'ROLE_admin', '系统管理员', '拥有最高权限', 1, '2021-03-22 16:50:43', '2021-04-08 16:58:55');
INSERT INTO `role` VALUES (2, 'ROLE_editor', '编辑', '可以对中转站的新闻进行加工编辑', 0, '2021-03-23 15:16:51', NULL);
INSERT INTO `role` VALUES (3, 'ROLE_deliverer', '传稿人', '可以上传新的新闻到中转站', 0, '2021-03-23 15:18:24', NULL);
INSERT INTO `role` VALUES (4, 'ROLE_reviewer1', '一审人员', '负责新闻的一审工作', 0, '2021-03-23 20:45:36', NULL);
INSERT INTO `role` VALUES (5, 'ROLE_reviewer2', '二审人员', '负责新闻的二审工作', 0, '2021-03-23 20:45:58', NULL);
INSERT INTO `role` VALUES (6, 'ROLE_reviewer3', '三审人员', '负责新闻的三审工作', 0, '2021-03-23 20:46:18', '2021-03-23 20:46:24');
INSERT INTO `role` VALUES (7, 'ROLE_publisher', '新闻发布员', '负责新闻的发布管理', 0, '2021-03-23 20:47:10', NULL);
INSERT INTO `role` VALUES (8, 'ROLE_columAdmin', '新闻栏目管理员', '负责管理新闻栏目', 0, '2021-03-23 20:48:07', NULL);
INSERT INTO `role` VALUES (9, 'ROLE_noticAdmin', '公告管理员', '负责门户网站的公告管理', 0, '2021-03-23 20:48:32', NULL);
INSERT INTO `role` VALUES (10, 'ROLE_linkAdmin', '友情链接管理员', '负责门户网站的友链管理', 0, '2021-03-23 20:49:06', NULL);
INSERT INTO `role` VALUES (11, 'ROLE_portalAdmin', '门户管理员', '负责门户网站的内容管理', 0, '2021-03-23 20:51:40', NULL);
INSERT INTO `role` VALUES (12, 'ROLE_nothing', '测试无权限', '不能访问任何资源', 0, '2021-03-25 21:31:52', NULL);
INSERT INTO `role` VALUES (109, 'ROLE_ceshi1', '测试添加1', '', 0, '2021-04-08 20:48:22', NULL);

-- ----------------------------
-- Table structure for role_resource
-- ----------------------------
DROP TABLE IF EXISTS `role_resource`;
CREATE TABLE `role_resource`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `role_id` int(0) NOT NULL,
  `resource_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_resource_unique`(`role_id`, `resource_id`) USING BTREE,
  INDEX `role_id_index`(`role_id`) USING BTREE,
  INDEX `resource_id_index`(`resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 339 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色_资源对应表，表示角色能访问哪些资源（包含父子资源）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_resource
-- ----------------------------
INSERT INTO `role_resource` VALUES (1, 1, 1);
INSERT INTO `role_resource` VALUES (2, 1, 2);
INSERT INTO `role_resource` VALUES (3, 1, 3);
INSERT INTO `role_resource` VALUES (4, 1, 4);
INSERT INTO `role_resource` VALUES (5, 1, 5);
INSERT INTO `role_resource` VALUES (6, 1, 6);
INSERT INTO `role_resource` VALUES (7, 1, 7);
INSERT INTO `role_resource` VALUES (8, 1, 8);
INSERT INTO `role_resource` VALUES (9, 1, 9);
INSERT INTO `role_resource` VALUES (10, 1, 10);
INSERT INTO `role_resource` VALUES (11, 1, 11);
INSERT INTO `role_resource` VALUES (344, 1, 12);
INSERT INTO `role_resource` VALUES (341, 1, 13);
INSERT INTO `role_resource` VALUES (342, 1, 14);
INSERT INTO `role_resource` VALUES (343, 1, 15);
INSERT INTO `role_resource` VALUES (16, 1, 16);
INSERT INTO `role_resource` VALUES (23, 1, 17);
INSERT INTO `role_resource` VALUES (294, 1, 24);
INSERT INTO `role_resource` VALUES (295, 1, 25);
INSERT INTO `role_resource` VALUES (96, 2, 3);
INSERT INTO `role_resource` VALUES (97, 2, 10);
INSERT INTO `role_resource` VALUES (98, 2, 11);
INSERT INTO `role_resource` VALUES (99, 2, 17);
INSERT INTO `role_resource` VALUES (297, 3, 3);
INSERT INTO `role_resource` VALUES (300, 3, 10);
INSERT INTO `role_resource` VALUES (296, 3, 17);
INSERT INTO `role_resource` VALUES (298, 3, 24);
INSERT INTO `role_resource` VALUES (299, 3, 25);
INSERT INTO `role_resource` VALUES (328, 4, 3);
INSERT INTO `role_resource` VALUES (335, 5, 3);
INSERT INTO `role_resource` VALUES (331, 6, 3);
INSERT INTO `role_resource` VALUES (332, 6, 10);
INSERT INTO `role_resource` VALUES (39, 9, 4);
INSERT INTO `role_resource` VALUES (18, 11, 3);
INSERT INTO `role_resource` VALUES (19, 11, 4);
INSERT INTO `role_resource` VALUES (20, 11, 5);
INSERT INTO `role_resource` VALUES (21, 11, 16);
INSERT INTO `role_resource` VALUES (24, 11, 17);

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `review_level` tinyint(0) NOT NULL DEFAULT 3 COMMENT '几轮审核',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, 3);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `real_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `passwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '加密后的密码',
  `gender` tinyint(0) NOT NULL COMMENT '0-女，1-男',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户备注',
  `locked` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用户是否锁定；0-false;1-true',
  `deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用户是否被删除;0-false,1-true',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_unique`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'PENG', '谢志朋', '$2a$10$ic9CfL9RTstDWcbVarK0XuVQ9z.00wnuw2KVlW87s09Tq59v0qk62', 1, '13907091944', 'ordinarypeng@163.com', 'PENG', 0, 0, '2021-03-22 16:48:26', '2021-04-06 23:14:15');
INSERT INTO `user` VALUES (2, '20210323', '张三', '$2a$10$ajBAjw5F8Gz8sw2NjeMQ0e9.8gJZ2qaRWqInENc7UCYVJIthGXJwi', 1, '13907091944', '2318511681@qq.com', NULL, 0, 0, '2021-03-23 15:21:54', NULL);
INSERT INTO `user` VALUES (3, '20210324', '李四', '$2a$10$ajBAjw5F8Gz8sw2NjeMQ0e9.8gJZ2qaRWqInENc7UCYVJIthGXJwi', 1, '13845679876', 'lisi@163.com', NULL, 0, 0, '2021-03-24 11:47:13', NULL);
INSERT INTO `user` VALUES (4, 'review1', '王五', '$2a$10$ajBAjw5F8Gz8sw2NjeMQ0e9.8gJZ2qaRWqInENc7UCYVJIthGXJwi', 0, '13945671234', 'wangwu@163.com', '一审人', 0, 0, '2021-03-24 15:23:33', NULL);
INSERT INTO `user` VALUES (5, 'review2', '钱六六', '$2a$10$ajBAjw5F8Gz8sw2NjeMQ0e9.8gJZ2qaRWqInENc7UCYVJIthGXJwi', 0, '18712345678', 'qianhaoduo@163.com', '二审人', 0, 0, '2021-03-24 15:24:16', NULL);
INSERT INTO `user` VALUES (6, 'review3', '赵七', '$2a$10$ajBAjw5F8Gz8sw2NjeMQ0e9.8gJZ2qaRWqInENc7UCYVJIthGXJwi', 0, '19198765432', 'zhaoqi@163.com', '三审人', 0, 0, '2021-03-24 15:24:58', NULL);
INSERT INTO `user` VALUES (7, 'test1', '测试1', '$2a$10$5AuQpiOAtBaRgAgknk2qlOnsrEzOG12tKLPQOof4Z21EGSWN8RUf2', 1, '13970668877', 'ceshi1@163.com', '我是测试用户1号', 0, 0, '2021-03-24 16:53:43', '2021-04-07 16:08:20');
INSERT INTO `user` VALUES (23, 't1', 'T1', '$10$5AuQpiOAtBaRgAgknk2qlOnsrEzOG12tKLPQOof4Z21EGSWN8RUf2', 1, '12131', '1213@qq.com', '大萨达', 0, 1, '2021-04-07 16:17:26', '2021-04-07 19:52:55');
INSERT INTO `user` VALUES (24, 't2', 'T2', '$2a$10$ajBAjw5F8Gz8sw2NjeMQ0e9.8gJZ2qaRWqInENc7UCYVJIthGXJwi', 1, '12102812', '123@qq.com', '我是T2', 0, 0, '2021-04-07 16:21:42', '2021-04-08 20:24:17');
INSERT INTO `user` VALUES (25, 't3', 'T3', '$10$5AuQpiOAtBaRgAgknk2qlOnsrEzOG12tKLPQOof4Z21EGSWN8RUf2', 0, '231102812', '123@qq.com', '我是T3', 0, 0, '2021-04-07 16:21:42', '2021-04-07 19:52:11');
INSERT INTO `user` VALUES (26, 't4', 'T4', '$2a$10$ic9CfL9RTstDWcbVarK0XuVQ9z.00wnuw2KVlW87s09Tq59v0qk62', 1, '2102812', '123@qq.com', '我是T4', 0, 0, '2021-04-07 16:21:42', '2021-04-07 21:40:29');
INSERT INTO `user` VALUES (27, 't5', 'T5', '$10$5AuQpiOAtBaRgAgknk2qlOnsrEzOG12tKLPQOof4Z21EGSWN8RUf2', 1, '2102812', '123@qq.com', '我是T5', 0, 1, '2021-04-07 16:21:42', '2021-04-07 19:53:09');
INSERT INTO `user` VALUES (28, 't6', 'T6', '$10$5AuQpiOAtBaRgAgknk2qlOnsrEzOG12tKLPQOof4Z21EGSWN8RUf2', 0, '2102812', '123@qq.com', '我是T6', 0, 1, '2021-04-07 16:21:42', '2021-04-07 19:53:19');
INSERT INTO `user` VALUES (29, 't7', 'T7', '$10$5AuQpiOAtBaRgAgknk2qlOnsrEzOG12tKLPQOof4Z21EGSWN8RUf2', 0, '2102812', '123@qq.com', '我是T7', 0, 0, '2021-04-07 16:21:42', '2021-04-07 19:52:11');
INSERT INTO `user` VALUES (30, 't8', 'T8', '$10$5AuQpiOAtBaRgAgknk2qlOnsrEzOG12tKLPQOof4Z21EGSWN8RUf2', 1, '2102812', '123@qq.com', '我是T8', 0, 0, '2021-04-07 16:21:42', '2021-04-07 19:52:11');
INSERT INTO `user` VALUES (82, 'PENG1', '谢小鹏', '$2a$10$/h.hEhWvSZUpS61jsT6gsu5c3lI5wBqeIX120qZGK2Ns3RSoD0j5y', 1, '13920210408', '231851@qq.com', '小名', 0, 0, '2021-04-08 10:40:51', NULL);
INSERT INTO `user` VALUES (83, '逍遥子', '武义', '$2a$10$fyXUnrJudy4bGittCFBcdOPHIqhMLLZgY7uNqfWqy2M2sM/L78I0S', 1, '13920210408', 'xiaoyaozi@163.com', '逍遥子', 0, 0, '2021-04-08 10:43:26', NULL);
INSERT INTO `user` VALUES (84, '风清扬', '风清扬', '$2a$10$VttQsVl9eetX3em8oFmEK.dGkn7znhtRg6Du/qCryoGMPVwdGdFuS', 0, '19120210408', 'fengqingyang@163.com', '', 0, 0, '2021-04-08 10:46:26', NULL);
INSERT INTO `user` VALUES (85, '小龙女', '小龙女', '$2a$10$ic9CfL9RTstDWcbVarK0XuVQ9z.00wnuw2KVlW87s09Tq59v0qk62', 0, '13720210408', 'xiaolongnv@163.com', '小龙女', 0, 0, '2021-04-08 10:51:16', '2021-04-08 19:44:04');
INSERT INTO `user` VALUES (86, 'ceshi2', '测试2', '$2a$10$ZyBV6kwUWrAIGDY/RfdHA.p1vCLNhQnq.qCL0mh22Ea9lWwHDMTJO', 1, '19720210408', '', '', 0, 0, '2021-04-08 21:31:54', NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL,
  `role_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (4, 3, 11);
INSERT INTO `user_role` VALUES (7, 6, 6);
INSERT INTO `user_role` VALUES (26, 2, 2);
INSERT INTO `user_role` VALUES (27, 2, 3);
INSERT INTO `user_role` VALUES (31, 1, 1);
INSERT INTO `user_role` VALUES (32, 7, 3);
INSERT INTO `user_role` VALUES (36, 4, 2);
INSERT INTO `user_role` VALUES (37, 4, 3);
INSERT INTO `user_role` VALUES (38, 4, 4);
INSERT INTO `user_role` VALUES (60, 84, 2);
INSERT INTO `user_role` VALUES (61, 84, 3);
INSERT INTO `user_role` VALUES (62, 85, 2);
INSERT INTO `user_role` VALUES (63, 85, 3);
INSERT INTO `user_role` VALUES (64, 85, 4);
INSERT INTO `user_role` VALUES (65, 85, 5);
INSERT INTO `user_role` VALUES (66, 85, 6);
INSERT INTO `user_role` VALUES (68, 24, 12);
INSERT INTO `user_role` VALUES (70, 86, 113);
INSERT INTO `user_role` VALUES (98, 5, 3);
INSERT INTO `user_role` VALUES (99, 5, 4);
INSERT INTO `user_role` VALUES (100, 5, 5);

-- ----------------------------
-- View structure for role_resource_view
-- ----------------------------
DROP VIEW IF EXISTS `role_resource_view`;
CREATE ALGORITHM = UNDEFINED DEFINER = `PENG`@`%` SQL SECURITY DEFINER VIEW `role_resource_view` AS select `t1`.`role_id` AS `role_id`,`t2`.`id` AS `id`,`t2`.`url` AS `url`,`t2`.`name` AS `name`,`t2`.`path` AS `path`,`t2`.`component` AS `component`,`t2`.`icon_cls` AS `icon_cls`,`t2`.`parent_id` AS `parent_id`,`t2`.`enabled` AS `enabled`,`t2`.`description` AS `description` from (`role_resource` `t1` join `resource` `t2` on((`t1`.`resource_id` = `t2`.`id`)));

SET FOREIGN_KEY_CHECKS = 1;
