/*
 * Disclaimer:
 * Despite the program working for auxiliary data
 * and the example, it did not give the right answer
 * for the puzzle input. For demonstration of the principle
 * and in hope someone finds the mistake, I published it
 * anyways.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) throws Exception {
		ArrayList<String> input = new ArrayList<String>();
		String path = "src\\input.txt";
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String str;
			while((str = br.readLine()) != null) {
				input.add(str);
			}
		}
		Stream<String> inputStream = input.stream().sorted((p1, p2) -> parseTime(p1).compareTo(parseTime(p2)));
		Iterator<String> iterator = inputStream.iterator();
		String str;
		int currentId = -1;
		// -1 stands for awake
		int lastTimeStamp = -1;
		HashMap<Integer, Integer[]> schedule = new HashMap<Integer, Integer[]>();
		while(iterator.hasNext()) {
			if((str = iterator.next()).charAt(25) == '#') {
				if(lastTimeStamp != -1) {
					Integer[] temp = schedule.get(currentId);
					for(int i = lastTimeStamp; i<60 ;i++) {
						temp[i] += 1;
					}
					schedule.put(currentId, temp);
				}
				currentId = Integer.parseInt(str.substring(26, 28));
				if(!schedule.containsKey(currentId)) {
					schedule.put(currentId, initArr(60));
				}
				lastTimeStamp = -1;
				continue;
			}
			if(str.charAt(25) == 'u') {
				Integer[] temp = schedule.get(currentId);
				for(int i = lastTimeStamp; i<parseTime(str).getMinute() ;i++) {
					temp[i] += 1;
				}
				lastTimeStamp = -1;
				continue;
			}
			if(str.charAt(25) == 'a') {
				lastTimeStamp = parseTime(str).getMinute();
				continue;
			}
		}		
		int id = schedule.keySet().stream()
					     .max((e1,e2) -> Integer.compare(sumArray(schedule.get(e1)), sumArray(schedule.get(e2))))
					     .get();
		int minute, aux, mostSleep;
		minute = aux = mostSleep = 0;
		for(int index = 0; index < 60; index++) {
			if((aux = schedule.get(id)[index]) > mostSleep) {
				mostSleep = aux;
				minute = index;
			}
		}	
		System.out.println("answer for (a): " + (minute * id));
	}
	static LocalDateTime parseTime(String str) {
		String[] temp = str.substring(1, 17).split("-| |:");
		List<Integer> l = Stream.of(temp).map(Integer::parseInt).collect(Collectors.toList());
		return LocalDateTime.of(l.get(0), l.get(1), l.get(2), l.get(3), l.get(4));
	}
	static Integer[] initArr(int length) {
		Integer[] result = new Integer[length];
		for(int index = 0; index<result.length; index++) {
			result[index] = 0;
		}
		return result;
	}
	static int sumArray(Integer[] intArr) {
		// ignore case "value does not exists"
		return Stream.of(intArr).reduce((e1,e2) -> e1+e2).get();
	}
}
