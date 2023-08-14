import java.util.*;


/*
Purpose of app:


    In this case, there are many "bots" that are being trained.
    You give the program a list of questions and an answer key for them
    It will use random reasoning to select an answer for the questions.
    The bot that gets the highest score's reasoning will be passed down.
    The same will keep happening until a "bot" gets a perfect score.
    The code will return some code that represents the reasoning of the bot with a perfect score.
    Then, you can go back to the program and use that, and test its reasoning with a quiz or add more to it.
*/

public class Main{
    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;
        while (i < tokens.length) {
            if (tokens[i] == ' ') {
                i++;
                continue;
            }

            if (Character.isDigit(tokens[i]) || tokens[i] == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    sb.append(tokens[i++]);
                }
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
                i++;
            } else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop(); // Remove the '(' from the stack
                i++;
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!operators.isEmpty() && hasPrecedence(tokens[i], operators.peek())) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(tokens[i]);
                i++;
            } else {
                throw new IllegalArgumentException("Invalid character: " + tokens[i]);
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private static double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    static int varCount = 0;

    private static boolean hasPrecedence(char operator1, char operator2) {
        return (operator2 != '(' && operator2 != ')')
                && (operator1 == '*' || operator1 == '/')
                && (operator2 == '+' || operator2 == '-');
    }

    public static int solveEquation(String ques, List<Question> questions){
        int i = 0;
        for (Question q:questions){
            if (q.question.startsWith("var_") && ques.contains(q.question)){
                System.out.println(q.question);
                ques = ques.replace(q.question, q.answer);
                if (ques.charAt(0) == '\"'){
                    questions.set(i, new Question(ques, Integer.toString(varCount++)));
                }else{
                questions.set(i, new Question(ques, Integer.toString((int)evaluate(ques))));
                }
            };
            i++;
        };
        if (ques.charAt(0) == '\"'){
            int ret = -1;
            for (Question q: questions){
                if (q.question == ques){
                    return Integer.parseInt(q.answer);
                }else if(q.question.substring(1, q.question.length()-1).contains(ques.substring(1, ques.length()-1))){
                    ret = Integer.parseInt(q.answer);
                }else if(ques.substring(1, ques.length()-1).contains(q.question.substring(1, q.question.length()-1))){
                    ret = Integer.parseInt(q.answer);
                };
            }
            return ret;
        };
        if(evaluate(questions.get(0).question) == Integer.parseInt(questions.get(0).answer)*1.0){
            int all = 0;
            for (Question q: questions){
                if (evaluate(q.question) ==  Integer.parseInt(q.answer)*1.0){
                    all++;
                }
            };
            if (all == questions.size()){
                return (int)evaluate(ques);
            }
        }else if (Integer.parseInt(questions.get(0).question) ==  Integer.parseInt(questions.get(0).answer)){
            int all = 0;
            for (Question q: questions){
                if (Integer.parseInt(q.question) ==  Integer.parseInt(q.answer)){
                    all++;
                }
            };
            if (all == questions.size()){
                return Integer.parseInt(ques);
            };
        }else if(Integer.parseInt(questions.get(0).question) > Integer.parseInt(questions.get(0).answer)){
            int all = 0;

            int gt = Integer.parseInt(questions.get(0).question) - Integer.parseInt(questions.get(0).answer);

            for (Question q: questions){
                if (Integer.parseInt(q.question)-gt ==  Integer.parseInt(q.answer)){
                    all++;
                }
            };;
            if (all == questions.size()){
                return Integer.parseInt(ques) - gt;
            }else{
                gt = Integer.parseInt(questions.get(0).question) / Integer.parseInt(questions.get(0).answer);
                all = 0;
                for (Question q: questions){
                    if (Integer.parseInt(q.question)/gt ==  Integer.parseInt(q.answer)){
                        all++;
                    }
                };
                if (all == questions.size()){
                    return Integer.parseInt(ques)/gt;
                }else{
                    gt = Integer.parseInt(questions.get(0).question) *Integer.parseInt( questions.get(0).answer);
                    all = 0;
                    for (Question q: questions){
                        if (Integer.parseInt(q.question)*gt ==  Integer.parseInt(q.answer)){
                            all++;
                        }
                    };
                    if (all == questions.size()){
                        return Integer.parseInt(ques)*gt;
                    }else{
                        gt = Integer.parseInt(questions.get(0).question) + Integer.parseInt(questions.get(0).answer);
                        all = 0;
                        for (Question q: questions){
                            if (Integer.parseInt(q.question)+gt ==  Integer.parseInt(q.answer)){
                                all++;
                            }
                        };
                        if (all == questions.size()){
                            return Integer.parseInt(ques)+gt;
                        }else{
                            gt = Integer.parseInt(questions.get(0).question) - Integer.parseInt(questions.get(0).answer);
                            all = 0;
                            for (Question q: questions){
                                if (Integer.parseInt(q.question)-gt ==  Integer.parseInt(q.answer)){
                                    all++;
                                }
                            };
                            if (all == questions.size()){
                                return Integer.parseInt(ques)-gt;
                            }
                        }
                    }
                };
            };
        }else if(Integer.parseInt(questions.get(0).question) < Integer.parseInt(questions.get(0).answer)){
            int all = 0;

            int gt = Integer.parseInt(questions.get(0).answer) - Integer.parseInt(questions.get(0).question);

            for (Question q: questions){
                if (Integer.parseInt(q.answer)-gt ==  Integer.parseInt(q.question)){
                    all++;
                }
            };
            if (all == questions.size()){
                return Integer.parseInt(ques) + gt;
            }else{
                    gt = Integer.parseInt(questions.get(0).answer) / Integer.parseInt(questions.get(0).question);
                    all = 0;
                    // System.out.println("First Question: "+Integer.parseInt(questions.get(0).question) +", Answer: "+questions.get(0).answer);
                    for (Question q: questions){
                        if (Integer.parseInt(q.question)*gt ==  Integer.parseInt(q.answer)){
                            all++;
                        }
                    };
                    if (all == questions.size()){
                        return Integer.parseInt(ques)*gt;
                    }else{
                        gt = Integer.parseInt(questions.get(0).question) + Integer.parseInt(questions.get(0).answer);
                        all = 0;
                        for (Question q: questions){
                            if (Integer.parseInt(q.question)+gt ==  Integer.parseInt(q.answer)){
                                all++;
                            }
                        };
                        if (all == questions.size()){
                            return Integer.parseInt(ques)+gt;
                        }else{
                            gt = Integer.parseInt(questions.get(0).question) - Integer.parseInt(questions.get(0).answer);
                            all = 0;
                            for (Question q: questions){
                                if (Integer.parseInt(q.question)-gt ==  Integer.parseInt(q.answer)){
                                    all++;
                                }
                            };
                            if (all == questions.size()){
                                return Integer.parseInt(ques)-gt;
                            }
                        }
                    }
                };
        };
        return -1;
    };

    static int grade(List<Question> given, List<Question> correctAnswers){
        int score = 0;
        int iw = 0;
        for (Question q: given){
            if (q.answer.equals(correctAnswers.get(iw).answer)){
                score++;
            };
            iw++;
        };
        int missed = given.size()-score;
        return 100 - (100/given.size())*missed;
    };

    static Question question(String ques, List<Question> questions){
        return new Question(ques, Integer.toString(solveEquation(ques, questions)));
    };
    
    public static void main(String[] args) throws Exception{
        List<Question> questions = new ArrayList<Question>();
        List<Question> correctAnswers = new ArrayList<Question>();
        questions.add(new Question("5", "25"));
        correctAnswers.add(new Question("5", "25"));
        questions.add(new Question("9", "45"));
        correctAnswers.add(new Question("9", "45"));
        questions.add(question("3", questions));
        correctAnswers.add(new Question("3", "15"));
        System.out.println("Score: "+grade(questions, correctAnswers));
    }
    // public static void main(String[] args) throws Exception{
    //     System.out.print("\u001B[32m");
    //     List<Question> questions = new ArrayList<Question>();
    //     int i = 1;
    //     int count = 0;
    //     int totalCount = 0;
    //     while(true){
    //     if (i % 3 == 1){
    //         questions.add(new Question("2", "1"));
    //         i *= 33;
    //     }else if(i % 3 == 2){
    //         questions.add(new Question("2", "3"));
    //         i+=totalCount%3;
    //     }
    //     else{
    //         questions.add(new Question("2", "2"));
    //         i -= i*70;
    //     };
    //     int res = solveEquation("1", questions);
    //     if (res == 0){
    //         System.out.print("0");
    //     }else if (res == 2){
    //         count+=count/4;
    //         System.out.print(" ");
    //     }else if (res == 1){
    //         System.out.print(res);
    //     };
    //     if (count >= 50){
    //         System.out.println("");
    //         for (int iw=0; iw<totalCount/2; iw++){
    //             System.out.print(" ");
    //         };
    //         totalCount++;
    //         count = 0;
    //     };
    //     questions.remove(questions.size()-1);
    //     count++;
    //     }
    //     // System.out.print("\u001B[37m");
    // }

}