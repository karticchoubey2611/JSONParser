public class JSONParser {
    private String input;
    private int index;

    public JSONParser(String input) {
        this.input = input;
        this.index = 0;
    }

    public JSONObject parseObject() {
        JSONObject obj = new JSONObject();
        consumeWhitespace();
        consume('{');
        while (true) {
            String key = parseString();
            consume(':');
            Object value = parseValue();
            obj.put(key, value);
            if (!consumeOptional(',')) {
                break;
            }
        }
        consume('}');
        return obj;
    }

    public JSONArray parseArray() {
        JSONArray arr = new JSONArray();
        consumeWhitespace();
        consume('[');
        while (true) {
            arr.add(parseValue());
            if (!consumeOptional(',')) {
                break;
            }
        }
        consume(']');
        return arr;
    }

    private Object parseValue() {
        consumeWhitespace();
        char next = peek();
        if (next == '{') {
            return parseObject();
        } else if (next == '[') {
            return parseArray();
        } else if (next == '"') {
            return parseString();
        } else if (next == 't') {
            consume("true");
            return true;
        } else if (next == 'f') {
            consume("false");
            return false;
        } else if (next == 'n') {
            consume("null");
            return null;
        } else {
            return parseNumber();
        }
    }

    private String parseString() {
        consume('"');
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = consume();
            if (c == '"') {
                break;
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private Number parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(peek()) || peek() == '.') {
            sb.append(consume());
        }
        String num = sb.toString();
        if (num.contains(".")) {
            return Double.parseDouble(num);
        } else {
            return Long.parseLong(num);
        }
    }

    private char consume() {
        consumeWhitespace();
        return input.charAt(index++);
    }

    private void consumeWhitespace() {
        while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
            index++;
        }
    }

    private void consume(char expected) {
        char actual = consume();
        if (actual != expected) {
            throw new RuntimeException("Expected: " + expected + ", found: " + actual);
        }
    }

    private boolean consumeOptional(char expected) {
        if (peek() == expected) {
            consume();
            return true;
        } else {
            return false;
        }
    }

    private char peek() {
        return input.charAt(index);
    }

    private void consume(String expected) {
        for (char c : expected.toCharArray()) {
            consume(c);
        }
    }

    public static void main(String[] args) {
        String jsonString = "{\"id\": 1, \"firstname\": \"Katerina\", \"languages\": [{\"lang\": \"en\", \"knowledge\": \"proficient\"}, {\"lang\": \"fr\", \"knowledge\": \"advanced\"}], \"job\": {\"site\": \"www.javacodegeeks.com\", \"name\": \"Java Code Geeks\"}}";
        JSONParser parser = new JSONParser(jsonString);
        JSONObject jsonObject = parser.parseObject();
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("glossary"));
        System.out.println(jsonObject.get("job"));
        System.out.println(jsonObject.get("languages"));
        System.out.println(jsonObject.get("firstname"));
        JSONObject jobObject = (JSONObject) jsonObject.get("job");
        String site = (String) jobObject.get("site");
        System.out.println("Site: " + site);
        JSONArray languagesArray = (JSONArray) jsonObject.get("languages");
        JSONObject firstLanguageObject = (JSONObject) languagesArray.get(1);
        String knowledge = (String) firstLanguageObject.get("knowledge");
        System.out.println("Knowledge: " + knowledge);
    }
}
