// This program will extract micro f1 and macro f1 from results file generated using Weka Experimenter.
// different files in one go.
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Extract_F1_10fcv_10rep {
	public static void main(String[] args) throws IOException {
		File f1 = new File("/home/vishal/Desktop/citation_new_experiment/data_set_remake_CSA/4_NEW/experimenter/RBF/default.csv");
		
		FileReader fr1 = new FileReader(f1);
		FileWriter fw1 = new FileWriter(f1.getParent() + "/out" + f1.getName());
		BufferedReader in1=new BufferedReader(fr1);
		int count = 1;
		
		int exp = 0	;
		double summacf1 = 0, summicf1 = 0; 
		double sumwmicf1 = 0.0;
		fw1.write("Model, RunId, Parameters, Weighted Micro-F1, Avg. Macro-F1, Avg. Micro-F1\r\n");
		List<String> lines = Files.readAllLines(Paths.get(f1.getPath()), StandardCharsets.ISO_8859_1);
		//System.out.println(lines.get(0).split(",")[49]);
		for(int i = 1; i < lines.size(); i++, count++)
		{
			String str1 = lines.get(i);
			try{
				String word[] = str1.split(",");
				String filename = word[0] + "_" + word[3].split("[.]")[3];
				if(word[3].split("[.]")[3].equals("LibSVM"))
				{
					if(word[4].split("-")[2].trim().equals("K 0"))
						filename = filename + " Linear";
					else if(word[4].split("-")[2].trim().equals("K 1"))
						filename = filename + " Polynomial";
					else if(word[4].split("-")[2].trim().equals("K 2"))
						filename = filename + " RBF";
					else if(word[4].split("-")[2].trim().equals("K 3"))
						filename = filename + " Sigmoid";
				}
				if(count <= 10 )
				{
					double macrof1 = Double.parseDouble(word[53]);
					double unwmicrof1 = Double.parseDouble(word[54]);
					double wmacrof1 = Double.parseDouble(word[49]);
					summacf1 += macrof1; 
					summicf1 += unwmicrof1;
					sumwmicf1 += wmacrof1;
					if(count==10)
					{
						exp++;
						count = 0;
						double avgmacf1 = summacf1 / 10;
						double avgmicf1 = summicf1 / 10;
						double avgwmicf1 = sumwmicf1 / 10;
						avgmacf1 = Math.round(avgmacf1 * 10000) / 100.0;
						avgmicf1 = Math.round(avgmicf1 * 10000) / 100.0;
						avgwmicf1 = Math.round(avgwmicf1 * 10000) / 100.0;
						fw1.write(filename + "," + word[1] + "," + word[4] + "," + avgwmicf1 + "," + avgmacf1 + "," + avgmicf1 + "\r\n");
						summacf1 = 0; 
						summicf1 = 0; 
						sumwmicf1 = 0; 
					}
				}
			}catch(Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
			finally
			{
			}
		}
		System.out.println("No. of lines read: " + (lines.size() - 1 )+ " no. of experiments: " + exp);
		in1.close();
		fr1.close();
		fw1.close();
		System.out.println(" \n == Done. == ");
	} // END MAIN
} // END CLASS
