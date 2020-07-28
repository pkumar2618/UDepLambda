package deplambda.others;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deplambda.parser.TreeTransformerMain;
import deplambda.util.TransformationRuleGroups;
import edu.cornell.cs.nlp.spf.mr.lambda.FlexibleTypeComparator;
import edu.cornell.cs.nlp.spf.mr.lambda.LogicLanguageServices;
import edu.cornell.cs.nlp.spf.mr.language.type.MutableTypeRepository;
import org.apache.log4j.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class My_NlpPipeline {

  public static void main(String[] args) throws Exception {

    JsonParser jsonParser = new JsonParser();
    JsonObject sent =
            jsonParser
                    .parse(
                            "{\"sentence\":\"Obama is the president of United States. He won a Nobel prize. What sport does Sally Pearson compete in? Who was Titanic directed by?\"}")
                    .getAsJsonObject();
    System.out.print(sent.get(SentenceKeys.SENTENCE_KEY).getAsString());
//    int nthreads = 1;
//    Writer writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
//    BufferedWriter fout = new BufferedWriter(writer);
//    BlockingQueue<Runnable> queue = new ArrayBlockingQueue(nthreads);
//    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(nthreads, nthreads, 600L, TimeUnit.SECONDS, queue);
//    threadPool.setRejectedExecutionHandler(new RejectedExecutionHandler() {
//      public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//        try {
//          executor.getQueue().put(r);
//        } catch (InterruptedException var4) {
//          var4.printStackTrace();
//        }
//      }
//    });
    String[] arguments = {"annotators",
            "tokenize,ssplit,pos,lemma,ner,depparse",
            "tokenize.language",
            "en",
            "ssplit.eolonly",
            "true",
            "nthreads",
            "1"};
    Map<String, String> options = new HashMap<>();
    for (int i = 0; i < arguments.length; i += 2) {
      options.put(arguments[i], arguments[i + 1]);
    }

    NlpPipeline englishPipeline = new NlpPipeline(options);
    englishPipeline.processStream(System.in, System.out, 1, true);

//    fout.close();

    BufferedWriter bw = new BufferedWriter(new FileWriter("./testing.log"));
    bw.write(sent.get(SentenceKeys.SENTENCE_KEY).getAsString());
    bw.close();

//    NlpPipeline englishPipeline = new NlpPipeline(options);
//    englishPipeline.processSentence(sent);

//    My_NlpPipeline englishPipeline = new My_NlpPipeline(options);
//    int nthreads =
//        options.containsKey(SentenceKeys.NTHREADS) ? Integer.parseInt(options
//            .get(SentenceKeys.NTHREADS)) : 20;
//    englishPipeline.processStream(System.in, System.out, nthreads, true);
  }
}
