package kz.attractor.java.utils;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static Map<String, String> parseUrlEncoded(String rawLines, String delimeter){
        String[] pairs = rawLines.split(delimeter);
        Stream<Map.Entry<String, String>> stream = Arrays.stream(pairs)
                .map(Utils::decode)
                .filter(Optional::isPresent)
                .map(Optional::get);
        return stream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static List<Optional<String>> parseInputEncoded(String rawLines, String delimeter){
        String[] pairs = rawLines.split(delimeter);
        List<Optional<String>> inputs = new ArrayList<>();
            for (int i = 0; i < pairs.length; i++){
                inputs.add(decodeBook(pairs[i]));
            }
        return inputs;
    }
    public static List<Optional<String>> parseUpdateEncoded(String rawLines, String delimeter){
        String[] pairs = rawLines.split(delimeter);
        List<Optional<String>> inputs = new ArrayList<>();
        for (int i = 0; i < pairs.length; i++){
            inputs.add(decodeBook(pairs[i]));
        }
        return inputs;
    }
    public static String parseUrlEncodedBook(String rawLines){
            String line = String.valueOf(decodeBook(rawLines));
        return line;
    }
    public static Optional<String> decodeBook(String kv){
        if(!kv.contains("=")) return Optional.empty();

        String pair = kv;

        Charset utf8 = StandardCharsets.UTF_8;

        return Optional.of(URLDecoder.decode(pair,utf8));
    }



    public static Optional<Map.Entry<String, String>> decode(String kv){
        if(!kv.contains("=")) return Optional.empty();

        String[] pair = kv.split("=");
        if(pair.length != 2) return Optional.empty();

        Charset utf8 = StandardCharsets.UTF_8;
        String key = URLDecoder.decode(pair[0],utf8);
        String value = URLDecoder.decode(pair[1],utf8);

        return Optional.of(Map.entry(key,value));
    }
}
