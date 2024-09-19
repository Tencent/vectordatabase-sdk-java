/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.tcvectordb.examples;

import com.tencent.tcvectordb.client.VectorDBClient;
import com.tencent.tcvectordb.model.Collection;
import com.tencent.tcvectordb.model.Database;
import com.tencent.tcvectordb.model.DocField;
import com.tencent.tcvectordb.model.Document;
import com.tencent.tcvectordb.model.param.collection.*;
import com.tencent.tcvectordb.model.param.dml.*;
import com.tencent.tcvectordb.model.param.entity.AffectRes;
import com.tencent.tcvectordb.encoder.SparseVectorBm25Encoder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.tencent.tcvectordb.model.param.enums.EmbeddingModelEnum.BGE_BASE_ZH;

/**
 * VectorDB Java SDK usage example
 */
public class VectorDBExampleWithSparseVector {

    private static final String DBNAME = "book_4";
    private static final String COLL_NAME = "book_segments_sparse_3";
    private static final String COLL_NAME_ALIAS = "collection_alias_sparse_3";

    public static void main(String[] args) throws InterruptedException {
        // 创建VectorDB Client
        VectorDBClient client = CommonService.initClient();

        // 清理环境
        CommonService.anySafe(() -> client.dropDatabase(DBNAME));

        // 测试
        createDatabaseAndCollection(client);
        upsertData(client);
        queryData(client);
        updateAndDelete(client);
        deleteAndDrop(client);
        testFilter();
    }


    private static void createDatabaseAndCollection(VectorDBClient client) {
        // 1. 创建数据库
        System.out.println("---------------------- createDatabase ----------------------");
        Database db = client.createDatabase(DBNAME);

        // 2. 列出所有数据库
        System.out.println("---------------------- listCollections ----------------------");
        List<String> database = client.listDatabase();
        for (String s : database) {
            System.out.println("\tres: " + s);
        }
//        Database db = client.database(DBNAME);

        // 3. 创建 collection
        System.out.println("---------------------- createCollection ----------------------");
        CreateCollectionParam collectionParam = initCreateCollectionParam(COLL_NAME);
        db.createCollection(collectionParam);

        // 4. 列出所有 collection
//        Database db = client.database(DBNAME);
        System.out.println("---------------------- listCollections ----------------------");
        List<Collection> cols = db.listCollections();
        for (Collection col : cols) {
            System.out.println("\tres: " + col.toString());
        }

        // 5. 设置 collection 别名
        System.out.println("---------------------- setAlias ----------------------");
        AffectRes affectRes = db.setAlias(COLL_NAME, COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes.toString());


        // 6. describe collection
        System.out.println("---------------------- describeCollection ----------------------");
        Collection descCollRes = db.describeCollection(COLL_NAME);
        System.out.println("\tres: " + descCollRes.toString());

        // 7. delete alias
        System.out.println("---------------------- deleteAlias ----------------------");
        AffectRes affectRes1 = db.deleteAlias(COLL_NAME_ALIAS);
        System.out.println("\tres: " + affectRes1);

        // 8. describe collection
        System.out.println("---------------------- describeCollection ----------------------");
        Collection descCollRes1 = db.describeCollection(COLL_NAME);
        System.out.println("\tres: " + descCollRes1.toString());

    }

    private static List<Double> generateRandomVector(int dim){
        Random random = new Random();
        List<Double> vectors = new ArrayList<>();

        for (int i = 0; i < dim; i++) {
            double randomDouble = 0 + random.nextDouble() * (1.0 - 0.0);
            vectors.add(randomDouble);
        }
        return vectors;
    }

    private static void upsertData(VectorDBClient client) throws InterruptedException {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        List<String> texts = Arrays.asList(
                "富贵功名，前缘分定，为人切莫欺心。",
                "正大光明，忠良善果弥深。些些狂妄天加谴，眼前不遇待时临。",
                "细作探知这个消息，飞报吕布。",
                "布大惊，与陈宫商议。宫曰：“闻刘玄德新领徐州，可往投之。”布从其言，竟投徐州来。有人报知玄德。",
                "玄德曰：“布乃当今英勇之士，可出迎之。”糜竺曰：“吕布乃虎狼之徒，不可收留；收则伤人矣。"
        );

        List<List<Pair<Long, Float>>> sparseVectors = encoder.encodeTexts(texts);
        List<Document> documentList = new ArrayList<>(Arrays.asList(
                Document.newBuilder()
                        .withId("0001")
                        .withVector(generateRandomVector(768))
                        .withSparseVector(sparseVectors.get(0))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 21))
                        .addDocField(new DocField("segment", "富贵功名，前缘分定，为人切莫欺心。"))
                        .addDocField(new DocField("text", "富贵功名，前缘分定，为人切莫欺心。"))
                        .build(),
                Document.newBuilder()
                        .withId("0002")
                        .withVector(generateRandomVector(768))
                        .withSparseVector(sparseVectors.get(1))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 22))
                        .addDocField(new DocField("segment",
                                "正大光明，忠良善果弥深。些些狂妄天加谴，眼前不遇待时临。"))
                        .addDocField(new DocField("text",
                                "正大光明，忠良善果弥深。些些狂妄天加谴，眼前不遇待时临。"))
                        .build(),
                Document.newBuilder()
                        .withId("0003")
                        .withVector(generateRandomVector(768))
                        .withSparseVector(sparseVectors.get(2))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 23))
                        .addDocField(new DocField("segment", "细作探知这个消息，飞报吕布。"))
                        .addDocField(new DocField("text", "细作探知这个消息，飞报吕布。"))
                        .build(),
                Document.newBuilder()
                        .withId("0004")
                        .withVector(generateRandomVector(768))
                        .withSparseVector(sparseVectors.get(3))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 24))
                        .addDocField(new DocField("segment", "富贵功名，前缘分定，为人切莫欺心。"))
                        .addDocField(new DocField("text", "富贵功名，前缘分定，为人切莫欺心。"))
                        .build(),
                Document.newBuilder()
                        .withId("0005")
                        .withVector(generateRandomVector(768))
                        .withSparseVector(sparseVectors.get(4))
                        .addDocField(new DocField("bookName", "三国演义"))
                        .addDocField(new DocField("author", "罗贯中"))
                        .addDocField(new DocField("page", 25))
                        .addDocField(new DocField("segment",
                                "布大惊，与陈宫商议。宫曰：“闻刘玄德新领徐州，可往投之。"))
                        .addDocField(new DocField("text",
                                "布大惊，与陈宫商议。宫曰：“闻刘玄德新领徐州，可往投之。"))
                        .build()));
        System.out.println("---------------------- upsert ----------------------");
        InsertParam insertParam = InsertParam.newBuilder()
                .addAllDocument(documentList)
                .withBuildIndex(true)
                .build();
        collection.upsert(insertParam);

        // notice：upsert操作可用会有延迟
        Thread.sleep(1000 * 3);
    }

    private static void queryData(VectorDBClient client) {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);

        // query  查询
        // 1. query 用于查询数据
        // 2. 可以通过传入主键 id 列表或 filter 实现过滤数据的目的
        // 3. 如果没有主键 id 列表和 filter 则必须传入 limit 和 offset，类似 scan 的数据扫描功能
        // 4. 如果仅需要部分 field 的数据，可以指定 output_fields 用于指定返回数据包含哪些 field，不指定默认全部返回

        System.out.println("---------------------- query ----------------------");
        List<String> documentIds = Arrays.asList("0001", "0002", "0003", "0004", "0005");
        Filter filterParam = new Filter("bookName=\"三国演义\"");
        List<String> outputFields = Arrays.asList("id", "bookName", "segment");
        QueryParam queryParam = QueryParam.newBuilder()
                .withDocumentIds(documentIds)
                // 使用 filter 过滤数据
                .withFilter(filterParam)
                // limit 限制返回行数，1 到 16384 之间
                .withLimit(2)
                // 偏移
                .withOffset(1)
                // 指定返回的 fields
                .withOutputFields(outputFields)
                // 是否返回 vector 数据
                .withRetrieveVector(false)
                .build();
        List<Document> qdos = collection.query(queryParam);
        for (Document doc : qdos) {
            System.out.println("\tres: " + doc.toString());
        }

        // search稀疏向量搜索和向量搜索混合
//        List<Double> vector = Arrays.asList(0.011228000745177269,-0.01778145506978035,-0.0008420095546171069,0.058591078966856,0.025626985356211662,-0.009485375136137009,0.0044272118248045444,0.03963795304298401,-0.07349739968776703,0.033373408019542694,-0.017562853172421455,-0.06693584471940994,-0.0008283713832497597,-0.09282511472702026,0.019856665283441544,0.0029369608964771032,0.025461247190833092,0.043309904634952545,-0.0010940685169771314,-0.02834116853773594,0.024333607405424118,-0.06358671933412552,0.0004886172828264534,-0.003996695391833782,-0.005141071975231171,0.031137600541114807,-0.03720816969871521,0.03373042494058609,-0.005284181796014309,0.06811655312776566,0.024133838713169098,-0.008262764662504196,-0.02401343360543251,0.01113040093332529,-0.019650375470519066,0.02405945584177971,0.008977336809039116,-0.022214235737919807,0.010524755343794823,-0.06512012332677841,-0.0070442515425384045,0.039866313338279724,0.003659360809251666,-0.014790602959692478,0.03323712199926376,0.030882341787219048,0.012858539819717407,0.03626066818833351,-0.02777714841067791,-0.0006111871916800737,0.011951539665460587,0.21934707462787628,-0.009810359217226505,-0.006874813232570887,0.07120531797409058,0.028546100482344627,0.01646684668958187,0.06472000479698181,-0.024948634207248688,0.02457079477608204,0.015692222863435745,0.015110153704881668,0.04548252373933792,-0.034427206963300705,0.0035766353830695152,-0.0063577317632734776,-0.03640788421034813,-0.03309265896677971,0.016592761501669884,-0.0184243842959404,0.025951147079467773,0.0009172717691399157,-0.020834090188145638,-0.029650429263710976,-0.00929233431816101,-0.02037796750664711,0.005400816909968853,-0.003652009414508939,0.0004404079227242619,0.004375770688056946,0.04158478602766991,0.0022120948415249586,0.023043958470225334,0.02082330361008644,-0.03482123836874962,-0.039342183619737625,-0.022602153941988945,0.006933159194886684,0.020710067823529243,0.02493712119758129,0.008838232606649399,-0.01082011591643095,-0.04711829870939255,0.015358409844338894,-0.05259833112359047,0.03564944863319397,-0.02094186656177044,-0.014302685856819153,-0.0009043896570801735,0.060996394604444504,0.012411488220095634,-0.014214642345905304,-0.026238352060317993,-0.033097539097070694,0.019276197999715805,0.019927779212594032,-0.0897335335612297,0.04069192707538605,0.016967572271823883,0.012736618518829346,0.007248705718666315,-0.042650915682315826,-0.012213833630084991,0.029378674924373627,-0.01131758838891983,-0.017445610836148262,0.01528218761086464,-0.0010133420582860708,0.008936449885368347,-0.04863755777478218,0.006830527447164059,-0.0006641799700446427,-0.03017701581120491,0.0045319232158362865,-0.025610102340579033,-0.024471720680594444,-0.028333375230431557,0.04000883921980858,0.007346018683165312,0.013694453984498978,-0.010897736065089703,-0.009626672603189945,-0.05798979103565216,0.014471616595983505,0.005314654670655727,-0.027487266808748245,-0.020013470202684402,-0.02379809133708477,-0.019267942756414413,-7.192990597104654e-05,0.03751828148961067,0.051989246159791946,0.02207176946103573,0.015709249302744865,0.04722846299409866,-0.009285308420658112,0.048197753727436066,0.0047728074714541435,0.03828258439898491,0.011275202967226505,0.03798242658376694,0.052456334233284,-0.02123964950442314,0.10464958846569061,-0.00095760339172557,0.005099969916045666,-0.01903274841606617,0.033146798610687256,-0.001944964868016541,0.03544747456908226,0.05200682580471039,0.004140638280659914,-0.001272501889616251,-0.029995812103152275,-0.036847393959760666,0.07203970104455948,-0.03457489609718323,0.013243802823126316,-0.0024343973491340876,0.04353635758161545,0.011767414398491383,-0.03771014139056206,-0.03384077921509743,-0.06499378383159637,-0.08102220296859741,0.06842608004808426,-0.007303804624825716,0.007441019639372826,0.00384475733153522,-0.015521292574703693,0.001064897864125669,-0.017786353826522827,0.0228169746696949,-0.0017404690152034163,-0.08029986172914505,-0.023406831547617912,-0.02028321474790573,-0.03325213864445686,0.011658839881420135,-0.02427353709936142,0.02202378585934639,0.012910107150673866,-0.028136733919382095,0.042230330407619476,0.04168698564171791,0.015558804385364056,-0.06827197968959808,0.03491898998618126,-0.04686718061566353,0.027400435879826546,0.009755358099937439,0.025745362043380737,-0.009593848139047623,0.08101218193769455,-0.023704754188656807,0.02434670366346836,0.021201400086283684,-0.02352142333984375,0.02945014089345932,-0.006461021490395069,0.0025584660470485687,-0.045635148882865906,0.011546051129698753,-0.03846436366438866,0.002660631900653243,0.011385934427380562,-0.054820869117975235,-0.027538765221834183,-0.012130278162658215,0.02718573808670044,0.05753879249095917,-0.024369744583964348,0.013582611456513405,-0.08181896805763245,-0.04349633306264877,0.03299983590841293,-0.004695754963904619,-0.01692030020058155,-0.011063349433243275,-0.00014669759548269212,0.014689693227410316,-0.038261156529188156,-0.015512768179178238,-0.004468604456633329,-0.048229288309812546,0.006024762522429228,-0.04957364872097969,0.00813916977494955,-0.026627950370311737,-0.009431248530745506,-0.0024864417500793934,0.017048964276909828,-0.02847607247531414,-0.020705807954072952,0.004949160385876894,-0.015850678086280823,0.060216374695301056,-0.015156073495745659,-0.007588365115225315,-0.037351079285144806,0.04218784347176552,0.020519305020570755,-0.0010683962609618902,-0.01784857176244259,-0.001070767524652183,-0.06896337121725082,-0.013779735192656517,0.02152661606669426,-0.042298782616853714,-0.09644242376089096,-0.043055132031440735,-0.013538303785026073,0.04751390591263771,0.057626064866781235,-0.009500116109848022,0.00708649680018425,-0.024209508672356606,-0.08286508917808533,-0.007599340286105871,0.032298244535923004,0.06640364229679108,0.024041226133704185,0.004293238278478384,0.020563913509249687,-0.022327011451125145,0.053777556866407394,-0.018423566594719887,-0.035526152700185776,-0.05626888573169708,0.016877761110663414,0.006738790310919285,0.06473775207996368,0.004535271320492029,0.04972827062010765,-0.07362700998783112,-0.0030079265125095844,0.059974104166030884,-0.013675615191459656,-0.034515395760536194,0.014622291550040245,-0.03385680168867111,-0.025142354890704155,0.0254383347928524,0.001493200776167214,0.334794282913208,0.02830587700009346,-0.07777972519397736,0.043088871985673904,-0.01293167844414711,-0.005174708086997271,-0.00581933930516243,-0.07398725301027298,0.017567740753293037,0.022265968844294548,0.11928874999284744,-0.055033378303050995,0.02395421266555786,-0.011595740914344788,0.02233700640499592,-0.018687181174755096,-0.06958524882793427,0.0009067300125025213,-0.00466264970600605,-0.03959940746426582,0.025952531024813652,0.01941017434000969,-0.03903195634484291,-0.0022335450630635023,0.0676443874835968,0.029737481847405434,-0.015014370903372765,0.007381070405244827,-0.0484633669257164,0.023242061957716942,-0.03460437059402466,-0.013248912990093231,-0.012566791847348213,-0.02383880689740181,0.014195072464644909,0.009522831067442894,0.05593474581837654,-0.048447150737047195,0.024550078436732292,-0.01998014561831951,0.004192877560853958,-0.0007203511777333915,-0.06000881642103195,0.007732178084552288,0.003466855501756072,-0.03827047348022461,0.024848824366927147,0.013989027589559555,0.017047369852662086,0.024627817794680595,0.019813280552625656,0.010560467839241028,-0.007128795608878136,0.021267788484692574,-0.05038474127650261,0.01119161769747734,0.01583665981888771,-0.0710073783993721,0.0680229514837265,0.015228376723825932,0.012478475458920002,-0.0007914555608294904,0.04503822326660156,-0.022816233336925507,-0.029615147039294243,-0.04138007014989853,0.015486898832023144,-0.033105045557022095,-0.027106864377856255,-0.018030496314167976,0.026013921946287155,0.005876969546079636,0.02092624269425869,0.05155663192272186,0.0006067254580557346,-0.060211531817913055,0.059832267463207245,0.009218663908541203,0.0034593825694173574,0.00601182272657752,0.0525486096739769,-0.017541436478495598,-0.016692979261279106,-0.012630617246031761,0.011909610591828823,0.027539147064089775,0.030525386333465576,-0.052341122180223465,-0.04240258410573006,-0.015112871304154396,-0.011462653055787086,0.016128763556480408,0.01829593814909458,-0.009016308933496475,-0.04230230674147606,0.007138686720281839,0.011453477665781975,-0.025860127061605453,0.023999856784939766,0.020570704713463783,-0.028624074533581734,0.01875831000506878,0.0018658454064279795,0.02916049025952816,-0.057269223034381866,0.0036049848422408104,0.010065961629152298,0.007160463370382786,-0.056595977395772934,-0.03341779485344887,-0.053864892572164536,-0.006826115772128105,-0.03297588229179382,0.01128164678812027,0.022906454280018806,-0.0031081661581993103,0.0009896974079310894,-0.042085934430360794,-0.05885877087712288,0.0595456063747406,0.010770737193524837,-0.021534759551286697,-0.03322180360555649,-0.04332531988620758,-0.03466140478849411,0.022178784012794495,-0.01332541648298502,-0.05094131454825401,0.04912945628166199,0.024204950779676437,-0.01659112051129341,-0.042198680341243744,0.051028888672590256,0.02925165370106697,-0.017100905999541283,0.008829696103930473,0.00015294468903448433,-0.010915480554103851,-0.015350918285548687,-0.06739851087331772,0.049365222454071045,0.009364373981952667,-0.021441806107759476,0.005624465644359589,0.04153790697455406,0.031615592539310455,0.009158196859061718,0.011399844661355019,-0.036585573107004166,0.008067300543189049,0.04017193615436554,-0.0044442773796617985,-0.030282024294137955,0.011847498826682568,0.005726383533328772,-0.014980319887399673,0.01719740219414234,-0.05555087700486183,0.009031839668750763,-0.03642238676548004,-0.04240124672651291,-0.023465050384402275,-0.0525900200009346,-0.019889121875166893,0.007329991087317467,-0.0348493717610836,-0.012565468437969685,0.00489308126270771,-0.01772800274193287,-0.03134306147694588,0.04384829103946686,0.03994167596101761,0.027157746255397797,0.05475940182805061,0.06232653930783272,0.020926667377352715,0.023273082450032234,0.00895698368549347,-0.021297913044691086,-0.007659547030925751,-0.0017791312420740724,0.01271106954663992,0.0007611988694407046,-0.019898725673556328,0.01639355905354023,-0.029277022927999496,0.024333303794264793,-0.016217123717069626,-0.05249570310115814,0.06855613738298416,0.019668709486722946,-0.013475027866661549,0.040491845458745956,-0.012874310836195946,-0.03878059238195419,-0.007048196159303188,-0.010489973239600658,-0.018570009618997574,0.008968203328549862,-0.033673934638500214,-0.018318500369787216,-0.009717299602925777,0.022672802209854126,-0.031869225203990936,0.025763994082808495,-0.04749876633286476,-0.048569515347480774,0.015351547859609127,-0.006761960685253143,0.02838185615837574,-0.0014055072097107768,-0.05071890354156494,-0.0023035514168441296,0.016775229945778847,0.038840923458337784,-0.002485994016751647,0.05317004397511482,-0.012521522119641304,0.026492154225707054,0.041588228195905685,0.016285225749015808,-0.03644036501646042,-0.018341371789574623,-0.001415338832885027,-0.017596542835235596,-0.0425528883934021,0.006830585654824972,-0.05454989895224571,0.03156057000160217,-0.0042657870799303055,-0.023661328479647636,-0.017319653183221817,-0.02879355102777481,0.006474115885794163,-0.05250633880496025,0.025808880105614662,-0.02048797346651554,-0.05077805370092392,-0.014409005641937256,0.012029886245727539,0.012740569189190865,0.022116873413324356,0.03960123285651207,-0.03199855983257294,-0.008848310448229313,-0.07307054847478867,-0.03872452303767204,-0.029967492446303368,0.03927792236208916,-0.035551927983760834,0.02775380201637745,-0.0018905715551227331,0.013925244100391865,0.01124342530965805,0.012179143726825714,-0.022628935053944588,0.022103911265730858,0.049286894500255585,-0.07614579051733017,0.03758956119418144,0.006597153376787901,-0.009310507215559483,-0.05107691138982773,-0.007727780379354954,-0.009930762462317944,0.01052931509912014,-0.022319145500659943,0.017863847315311432,0.010758845135569572,0.013254022225737572,-0.000858207989949733,-0.025006303563714027,-0.02406756952404976,-0.008208317682147026,-0.010748173110187054,-0.00698865344747901,-0.038527026772499084,-0.003004539292305708,-0.02299804426729679,-0.009805059060454369,0.028496552258729935,0.01125089917331934,0.03175720199942589,-0.03186783194541931,-0.023211995139718056,-0.012901285663247108,-0.011883283965289593,-0.035922542214393616,-0.0054933540523052216,-0.03287219628691673,0.0075798616744577885,-0.017400970682501793,-0.06543558835983276,-0.07086843997240067,-0.02236025407910347,0.011312036775052547,0.013881144113838673,0.011187789030373096,-0.021352853626012802,0.01209521759301424,0.010986448265612125,0.0280466265976429,0.0028384842444211245,-0.012631536461412907,-0.013534322381019592,-0.015131715685129166,0.040526192635297775,0.03799454867839813,0.0175810307264328,0.0023343598004430532,0.004337592050433159,0.011728398501873016,0.00985186081379652,-0.04791787639260292,0.0003230224538128823,0.07999759912490845,-0.05766419321298599,0.044354550540447235,0.030577993020415306,0.03863826021552086,0.012316863052546978,-0.01816105842590332,0.010841290466487408,0.0041139451786875725,0.03662897273898125,0.0702822208404541,0.053291574120521545,-0.0105751883238554,-0.011610777117311954,0.006535766180604696,-0.04369916766881943,-0.050802748650312424,-0.019643759354948997,0.014163920655846596,0.05906696245074272,0.05733434855937958,0.015502110123634338,0.04357575252652168,0.052199672907590866,0.02231508120894432,0.013078796677291393,-0.007436834741383791,-0.003492080606520176,0.01568499580025673,-0.09041984379291534,-0.0508231446146965,0.03946860134601593,0.0001249655761057511,0.016918376088142395,0.00012592262646649033,-0.07463286072015762,-0.003312620334327221,-0.0365043468773365,-0.02361810766160488,-0.026499824598431587,0.07702545076608658,0.0030056070536375046,-0.021168861538171768,0.008991563692688942,-0.04691014811396599,0.031971074640750885,-0.0174985621124506,0.05094505473971367,-0.03515065833926201,0.04063650965690613,-0.001741324202157557,0.020261533558368683,-0.058702364563941956,-0.058434709906578064,-0.05214286968111992,0.058829329907894135,-0.034132201224565506,0.018815433606505394,0.015444993041455746,-0.019551441073417664,-0.007262536324560642,0.08271834999322891,0.019005021080374718,-0.031974907964468,0.022663820534944534,0.007609522435814142,-0.09004736691713333,-0.04941195249557495,-0.006096153520047665,-0.03533686697483063,0.005633222870528698,-0.041101887822151184,0.02555353380739689,-0.023150743916630745,-0.03864051774144173,-0.011613970622420311,-0.05419612675905228,0.03341307491064072,-0.03822252154350281,-0.0159537885338068,0.05595235154032707,-0.001516361953690648,-0.008176641538739204,0.04819527268409729,-0.012294626794755459,0.034582968801259995,0.03888314217329025,-0.03052809089422226,0.017518021166324615,0.0031483874190598726,0.011357742361724377,0.045062460005283356,-0.03600004315376282,-0.002399398246780038,0.004231774713844061,0.04196177423000336,0.054733987897634506,-0.032597851008176804,-0.022816237062215805,-0.0009701708913780749,0.011314227245748043,-0.048491064459085464,-0.009732344187796116,-0.035001225769519806,-0.009082093834877014,0.0709235668182373,-0.015347892418503761,-0.0015579608734697104,0.017158204689621925,-0.05991761386394501,-0.018858136609196663,-0.001102324342355132,-0.01681104116141796,0.03128630295395851,0.026118021458387375,-0.03130285069346428,0.0374942384660244,-0.0007803713670000434,-0.03938727825880051,0.028128813952207565,-0.0022652731277048588,-0.013466103002429008,-0.0370059572160244,-0.022594835609197617,0.003917117603123188,0.02420860342681408,0.04335761442780495,0.04469241946935654,0.051333390176296234,-0.023145707324147224,0.021898699924349785,-0.04395199567079544,0.010561014525592327,0.04481233283877373,-0.012639965862035751,0.06350893527269363,0.01094555202871561,0.03683890774846077,0.019029002636671066,0.020309962332248688,0.02548789419233799,-0.015589339658617973,0.013123778626322746,0.06714979559183121,-0.005883317440748215,-0.02384098246693611,0.034502606838941574,0.04979805275797844,0.026102885603904724,-0.010899506509304047,0.03565442934632301,0.010668277740478516,-0.04402590170502663,0.05710256099700928,0.05378012731671333,-0.003941510338336229,-0.013945785351097584,0.008478756062686443,-0.048995040357112885);
        System.out.println("---------------------- hybridSearch ----------------------");
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        HybridSearchParam hybridSearchParam = HybridSearchParam.newBuilder()
                .withAnn(Arrays.asList(AnnOption.newBuilder().withFieldName("vector")
                        .withData(generateRandomVector(768))
                        .build()))
                .withMatch(Arrays.asList(MatchOption.newBuilder().withFieldName("sparse_vector")
                        .withData(encoder.encodeQueries(Arrays.asList("正大光明，忠良善果弥深")))
                        .build()))
                // 指定 Top K 的 K 值
                .withRerank(new WeightRerankParam(Arrays.asList("vector","sparse_vector"), Arrays.asList(1, 1)))
                .withLimit(10)
                // 过滤获取到结果
                .withFilter(filterParam)
                .withRetrieveVector(true)
                .withOutputFields(Arrays.asList("segment"))
                .build();
        List<List<Document>> siDocs = collection.hybridSearch(hybridSearchParam).getDocuments();
        int i = 0;
        for (List<Document> docs : siDocs) {
            System.out.println("\tres: " + i++);
            for (Document doc : docs) {
                System.out.println("\tres: " + doc.toString());
            }
        }
    }

    private static void updateAndDelete(VectorDBClient client) throws InterruptedException {
        Database database = client.database(DBNAME);
        Collection collection = database.describeCollection(COLL_NAME);


        // update
        // 1. update 提供基于 [主键查询] 和 [Filter 过滤] 的部分字段更新或者非索引字段新增

        // filter 限制仅会更新 id = "0003"
        System.out.println("---------------------- update ----------------------");
        Filter filterParam = new Filter("bookName=\"三国演义\"");
        List<String> documentIds = Arrays.asList("0001", "0003");
        SparseVectorBm25Encoder encoder = SparseVectorBm25Encoder.getBm25Encoder("zh");
        UpdateParam updateParam = UpdateParam
                .newBuilder()
                .addAllDocumentId(documentIds)
                .withFilter(filterParam)
                .build();
        Document updateDoc = Document
                .newBuilder()
                .addDocField(new DocField("page", 100))
                // 支持添加新的内容
                .addDocField(new DocField("extend", "extendContent"))
                .withSparseVector(encoder.encodeQueries(Arrays.asList("正大光明，忠良善果弥深")).get(0))
                .build();
        collection.update(updateParam, updateDoc);

        // delete
        // 1. delete 提供基于[ 主键查询]和[Filter 过滤]的数据删除能力
        // 2. 删除功能会受限于 collection 的索引类型，部分索引类型不支持删除操作

        // filter 限制只会删除 id = "00001" 成功
        System.out.println("---------------------- delete ----------------------");
        filterParam = new Filter("bookName=\"西游记\"");
        DeleteParam build = DeleteParam
                .newBuilder()
                .addAllDocumentId(documentIds)
                .withFilter(filterParam)
                .build();
        collection.delete(build);

        // notice：delete操作可用会有延迟
        Thread.sleep(1000 * 5);

        // rebuild index
        System.out.println("---------------------- rebuild index ----------------------");
        RebuildIndexParam rebuildIndexParam = RebuildIndexParam
                .newBuilder()
                .withDropBeforeRebuild(false)
                .withThrottle(1)
                .build();
        collection.rebuildIndex(rebuildIndexParam);
        Thread.sleep(5 * 1000);


        // truncate 会清除整个 Collection 的数据，包括索引
        System.out.println("---------------------- truncate collection ----------------------");
        AffectRes affectRes = database.truncateCollections(COLL_NAME);
        System.out.println("\tres: " + affectRes.toString());

        Thread.sleep(5 * 1000);
    }

    private static void deleteAndDrop(VectorDBClient client) {
        Database database = client.database(DBNAME);

        // 删除 collection
        System.out.println("---------------------- dropCollection ----------------------");
        database.dropCollection(COLL_NAME);

        // 删除 database
        System.out.println("---------------------- dropDatabase ----------------------");
        client.dropDatabase(DBNAME);
    }


    private static void clear(VectorDBClient client) {
//        List<String> databases = client.listDatabase();
//        for (String database : databases) {
//            client.dropDatabase(database);
//        }
        client.dropDatabase(DBNAME);
    }


    /**
     * 初始化创建 Collection 参数
     * 通过调用 addField 方法设计索引（不是设计 Collection 的结构）
     * <ol>
     *     <li>【重要的事】向量对应的文本字段不要建立索引，会浪费较大的内存，并且没有任何作用。</li>
     *     <li>【必须的索引】：主键id、向量字段 vector、稀疏向量sparse_vector 这两个字段目前是固定且必须的，参考下面的例子；</li>
     *     <li>【其他索引】：检索时需作为条件查询的字段，比如要按书籍的作者进行过滤，这个时候author字段就需要建立索引，
     *     否则无法在查询的时候对 author 字段进行过滤，不需要过滤的字段无需加索引，会浪费内存；</li>
     *     <li>向量数据库支持动态 Schema，写入数据时可以写入任何字段，无需提前定义，类似MongoDB.</li>
     *     <li>例子中创建一个书籍片段的索引，例如书籍片段的信息包括 {id, vector, segment, bookName, author, page},
     *     id 为主键需要全局唯一，segment 为文本片段, vector 字段需要建立向量索引，假如我们在查询的时候要查询指定书籍
     *     名称的内容，这个时候需要对 bookName 建立索引，其他字段没有条件查询的需要，无需建立索引。/li>
     *     </li>
     * </ol>
     *
     * @param collName
     * @return
     */
    private static CreateCollectionParam initCreateCollectionParam(String collName) {
        return CreateCollectionParam.newBuilder()
                .withName(collName)
                .withShardNum(1)
                .withReplicaNum(0)
                .withDescription("test sparse collection0")
                .addField(new FilterIndex("id", FieldType.String, IndexType.PRIMARY_KEY))
                .addField(new VectorIndex("vector", BGE_BASE_ZH.getDimension(), IndexType.HNSW,
                        MetricType.IP, new HNSWParams(16, 200)))
                .addField(new FilterIndex("sparse_vector", FieldType.SparseVector, IndexType.INVERTED, MetricType.IP))
                .addField(new FilterIndex("bookName", FieldType.String, IndexType.FILTER))
                .addField(new FilterIndex("author", FieldType.String, IndexType.FILTER))
                .build();
    }

    /**
     * 测试 Filter
     */
    public static void testFilter() {
        System.out.println("\tres: " + new Filter("author=\"jerry\"")
                .and("a=1")
                .or("r=\"or\"")
                .orNot("rn=2")
                .andNot("an=\"andNot\"")
                .getCond());
        System.out.println("\tres: " + Filter.in("key", Arrays.asList("v1", "v2", "v3")));
        System.out.println("\tres: " + Filter.notIn("key", Arrays.asList(1, 2, 3)));
    }


}

