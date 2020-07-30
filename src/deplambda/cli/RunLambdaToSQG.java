package deplambda.cli;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deplambda.others.SentenceKeys;
import in.sivareddy.graphparser.cli.CcgParseToUngroundedGraphs;
import in.sivareddy.graphparser.parsing.GroundedGraphs;
import in.sivareddy.graphparser.parsing.LexicalGraph;
import in.sivareddy.graphparser.util.GroundedLexicon;
import in.sivareddy.graphparser.util.Schema;
import in.sivareddy.graphparser.util.knowledgebase.KnowledgeBaseCached;
import in.sivareddy.util.SentenceUtils;
import org.apache.log4j.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RunLambdaToSQG{
  JsonParser jsonParser;
  Gson gson;
  GroundedGraphs graphCreator;
  Logger logger;
  int nbestParses;

  public RunLambdaToSQG() throws IOException
  {
    jsonParser = new JsonParser();
    gson = new Gson();
    logger = Logger.getLogger(CcgParseToUngroundedGraphs.class);
    nbestParses = 10;
    Schema schema = null;
    KnowledgeBaseCached kb = new KnowledgeBaseCached(null, null);
    GroundedLexicon groundedLexicon = new GroundedLexicon(null);
    graphCreator = new GroundedGraphs(schema, kb, groundedLexicon,
            null, null,
            null, null, null, null, 1,
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, 10.0, 1.0, 0.0, 0.0, 0.0);
  }

  public List<List<LexicalGraph>> processText(String line)
          throws IOException, InterruptedException
  {
    List<List<LexicalGraph>> allGraphs = new ArrayList<>();
    JsonObject jsonSentence = jsonParser.parse(line).getAsJsonObject();
    if (jsonSentence.has("sentence"))
    {
      logger.debug("Input Sentence: " + jsonSentence.get("sentence").getAsString());
      String sentence = jsonSentence.get("sentence").getAsString();
//    List<String> processedText = nlpPipeline.processText(sentence);
      List<LexicalGraph> graphs = graphCreator.buildUngroundedGraph(jsonSentence, SentenceKeys.DEPENDENCY_LAMBDA,
                    nbestParses, logger);
      allGraphs.add(graphs);
    }
    return allGraphs;
  }

  public static void main(String[] args)
          throws IOException, InterruptedException {
//    String inputFileVal = "./dep_parse.txt";
//    InputStream fileInputStream = SentenceUtils.getInputStream(inputFileVal);
//    BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

    try {

      PatternLayout layout = new PatternLayout("%r [%t] %-5p: %m%n");
      Logger logger = Logger.getLogger(CcgParseToUngroundedGraphs.class);
      logger.setLevel(Level.DEBUG);
      logger.setAdditivity(false);
      Appender stdoutAppender = new ConsoleAppender(layout);
      logger.addAppender(stdoutAppender);

//            Prepare the SQG parser
      RunLambdaToSQG sqg = new RunLambdaToSQG();
      String line = br.readLine();
      while (line != null) {
        if (line.trim().equals("") || line.charAt(0) == '#') {
          line = br.readLine();
          continue;
        }

        List<List<LexicalGraph>> allGraphs = sqg.processText(line);
        for (List<LexicalGraph> graphs : allGraphs) {
          logger.debug("# Ungrounded Graphs");
          if (graphs.size() > 0) {
            for (LexicalGraph ungroundedGraph : graphs) {
              logger.debug(ungroundedGraph);
            }
          }
        }

        line = br.readLine();
      }
//      System.out.println("abinitio");
    }catch (IOException e){
      e.printStackTrace();
    }finally {
      br.close();
    }
  }
}
