package adventofcode.y2023;

import adventofcode.commons.AoCInput;
import adventofcode.commons.AoCProblem;
import adventofcode.commons.LineEx;

import java.util.*;

import static adventofcode.y2023.Problem20.Pulse.HIGH;
import static adventofcode.y2023.Problem20.Pulse.LOW;
import static java.util.Collections.EMPTY_LIST;
import static java.util.function.Predicate.not;

/**
 * Day 20: Pulse Propagation
 * https://adventofcode.com/2023/day/20
 */
public class Problem20 extends AoCProblem<Long> {

    public static void main(String[] args) throws Exception {
        new Problem20().solve(false);
    }

    public static final String BROADCASTER = "broadcaster";
    public static final String APTLY = "aptly";

    enum Pulse {
        HIGH, LOW
    }

    record Message(String from, String to, Pulse pulse) {}

    class MessageBus {

        long lowPulses = 0;
        long highPulses = 0;

        private LinkedList<Message> topic = new LinkedList<>();

        public void publish(Message message) {
            if (message.pulse == LOW) {
                lowPulses++;
            } else {
                highPulses++;
            }
            topic.add(message);
        }

        public void publish(Collection<Message> messages) {
            messages.forEach(this::publish);
        }

        public Message poll() {
            return topic.pollFirst();
        }
    }

    /**
     *
     */
    abstract class Module {

        final String id;
        final List<String> outputs;

        public Module(String id, List<String> outputs) {
            this.id = id;
            this.outputs = outputs;
        }

        @Override
        public String toString() {
            return id + "[" + this.getClass().getSimpleName() + "]";
        }

        void reset() {

        }

        void initInput(String input) {
        }

        abstract List<Message> consume(Message message);
    }

    /**
     * Flip-flop (prefix %) := on | off (off)
     *           high pulse -> ignored
     *           low pulse  -> it flips between on and off
     *                         if it was off, it turns on and sends a high pulse
     *                         if it was on, it turns off and sends a low pulse
     */
    class FlipFlop extends Module {

        boolean status = false;

        public FlipFlop(String id, List<String> outputs) {
            super(id, outputs);
        }

        @Override
        void reset() {
            status = false;
        }

        @Override
        List<Message> consume(Message message) {
            if (HIGH == message.pulse) return EMPTY_LIST;
            Pulse pulseToSend = status ? LOW : HIGH;
            status = !status;
            return outputs.stream()
                          .map(output -> new Message(id, output, pulseToSend))
                          .toList();
        }
    }

    /**
     * Conjunction (prefix &) remember the type of the most recent pulse received from connected input modules
     *           default remembering a low pulse for each input
     *           when a pulse is received
     *                        first updates its memory for that input
     *                        then, if it remembers high pulses for all inputs, it sends a low pulse
     *                        otherwise, it sends a high pulse
     */
    class Conjunction extends Module {

        Map<String, Pulse> memory = new HashMap<>();
        List<String> inputs = new ArrayList<>();

        public Conjunction(String id, List<String> outputs) {
            super(id, outputs);
        }

        @Override
        void reset() {
            memory.clear();
            inputs.clear();
        }

        @Override
        void initInput(String input) {
            if (memory.put(input, LOW) == null)
                inputs.add(input);
        }

        @Override
        List<Message> consume(Message message) {
            memory.put(message.from, message.pulse);
            boolean allHighs = memory.values().stream().allMatch(p -> p == HIGH);
            Pulse pulseToSend = allHighs ? LOW : HIGH;
            return outputs.stream()
                          .map(output -> new Message(id, output, pulseToSend))
                          .toList();
        }

        private String dumpInputs() {
            StringBuilder sb = new StringBuilder();
            for (String input : inputs)
                sb.append(memory.get(input) == HIGH ? "1" : "0");
            return sb.toString();
        }
    }

    /**
     * Broadcast module (named broadcaster) is a SINGLE module
     *           when it receives a pulse
     *                        it sends the same pulse to all of its destination modules
     */
    class Broadcast extends Module {

        public Broadcast(String id, List<String> outputs) {
            super(id, outputs);
        }

        @Override
        List<Message> consume(Message message) {
            return outputs.stream()
                          .map(output -> new Message(id, output, message.pulse))
                          .toList();
        }
    }

    /**
     *
     */
    class Output extends Module {

        String input;
        long lowPulses = 0;
        long highPulses = 0;

        public Output(String id) {
            super(id, EMPTY_LIST);
        }

        @Override
        void reset() {
            lowPulses = 0;
            highPulses = 0;
        }

        @Override
        void initInput(String input) {
            this.input = input;
        }

        @Override
        List<Message> consume(Message message) {
            if (message.pulse == LOW) {
                lowPulses++;
            } else {
                highPulses++;
            }
            // System.out.printf("receive %s from %s%n", message.pulse, message.from);
            return EMPTY_LIST;
        }
    }

    Map<String, Module> modules = new HashMap<>();

    @Override
    public void processInput(AoCInput input) throws Exception {
        for (LineEx line : input.iterateLineExs()) {
            Module m;
            // %a -> inv, con
            String id = line.before(" -> ").toString();
            List<String> outputs = line.after(" -> ").splitToStrings(", ");
            if (id.startsWith("%")) {
                m = new FlipFlop(id.substring(1), outputs);
            } else if (id.startsWith("&")) {
                m = new Conjunction(id.substring(1), outputs);
            } else if (id.equals(BROADCASTER)) {
                m = new Broadcast(id, outputs);
            } else {
                throw new IllegalStateException();
            }
            modules.put(m.id, m);
        }
    }

    private void reset() {
        // reset all modules
        modules.values().forEach(Module::reset);

        // I want to have FlipFlop sorted so I can print bits in the right order
        Broadcast broadcaster = (Broadcast) modules.get(BROADCASTER);
        List<Module> sortedByDistanceFromBroadcast = new ArrayList<>(modules.size());
        Set<Module> visited = new HashSet<>();
        Deque<Module> stack = new LinkedList<>();
        stack.add(broadcaster);
        while (!stack.isEmpty()) {
            Module m = stack.pollFirst();
            if (m instanceof Conjunction) continue; // leave Conjunction at the end
            if (visited.contains(m)) continue;
            visited.add(m);
            sortedByDistanceFromBroadcast.add(m);
            m.outputs.stream()
                     .map(modules::get)
                     .filter(i -> i != null)
                     .forEach(stack::add);
        }
        modules.values().stream()
               .filter(not(visited::contains))
               .forEach(sortedByDistanceFromBroadcast::add);

        // init inputs
        sortedByDistanceFromBroadcast.forEach(m -> {
            m.outputs.stream()
                     .map(modules::get)
                     .filter(not(Objects::isNull))
                     .forEach(output -> output.initInput(m.id));
        });
    }

    /**
     * ...Consult your module configuration; determine the number of low pulses and
     * high pulses that would be sent after pushing the button 1000 times, waiting
     * for all pulses to be fully handled after each push of the button.
     * What do you get if you multiply the total number of low pulses sent by the
     * total number of high pulses sent?
     */
    @Override
    public Long partOne() throws Exception {
        MessageBus bus = new MessageBus();
        reset();

        for (int i = 0; i < 1000; ++i)
            pushButton(bus, null);

        // System.out.println("low:  " + bus.lowPulses);
        // System.out.println("high: " + bus.highPulses);
        long result = bus.lowPulses * bus.highPulses;
        return result;
    }

    private List<Message> pushButton(MessageBus bus, String moduleToSniff) {
        List<Message> sniff = null;
        bus.publish(new Message(APTLY, BROADCASTER, LOW));
        while (!bus.topic.isEmpty()) {
            Message message = bus.poll();
            Module module = modules.get(message.to);
            if (module != null) {
                if (moduleToSniff != null && module.id.equals(moduleToSniff)) {
                    if (sniff == null) sniff = new ArrayList<>();
                    sniff.add(message);
                }
                List<Message> messages = module.consume(message);
                bus.publish(messages);
            }
        }
        return sniff;
    }

    /**
     * ...The final machine responsible for moving the sand down to Island Island has a
     * module attached named rx. The machine turns on when a single low pulse is sent to rx.
     *
     * ...Reset all modules to their default states. Waiting for all pulses to be fully
     * handled after each button press, what is the fewest number of button presses
     * required to deliver a single low pulse to the module named rx?
     */
    @Override
    public Long partTwo() throws Exception {
        // After analyzing the module graph using Graphviz/dot (the input structure
        // is quite similar to its language), it is evident that there are groups
        // of FlipFlops (in this case, 4 groups of 12) that influence a small number
        // of Conjunctions.
        // These, in turn, converge into a single Conjunction that determines the
        // value of the module where the pulse is expected.
        //
        //The idea is to analyze the frequency at which HIGH pulses are received
        // from these inputs, collect at least 4 (or more), verify if they occur
        // repeatedly for each pulse with a regular period, and, in such cases,
        // compute the least common multiple (LCM) of the periods.

        MessageBus bus = new MessageBus();
        // add new module "rx"
        Output rx = new Output("rx");
        modules.put("rx", rx);
        reset();

        // get "output" conjuction that send data to rx
        Conjunction outputConjuction = (Conjunction) modules.get(rx.input);
        int inputSize = outputConjuction.inputs.size();
        Map<String, List<Long>> pulseStats = new HashMap<>();

        // collects 4 or more pulses for any input of the conjuction
        long count = 0;
        countloop:
        while (rx.lowPulses == 0) {
            count++;
            List<Message> messages = pushButton(bus, outputConjuction.id);
            for (Message m : messages) {
                if (m.pulse == HIGH) {
                    // System.out.printf("%16d - %3s -> %s%n", count, m.from, m.pulse);
                    List<Long> samples = pulseStats.computeIfAbsent(m.from, k -> new ArrayList<>());
                    samples.add(count);
                    if (pulseStats.size() == inputSize && pulseStats.values().stream().allMatch(s -> s.size() > 3))
                        break countloop;
                }
            }
        }

        // check if we can find period between samples of each input
        for (var l : pulseStats.values()) {
            for (int i = 1; i < 4; ++i) {
                if (l.get(0) * (i + 1) != l.get(i))
                    return -1L; // cannot find period
            }
        }

        // find LCM of the values
        long[] values = pulseStats.values().stream()
                                  .mapToLong(l -> l.get(0))
                                  .toArray();
        long result = lcm(values);
        return result;
    }

    public static long lcm(long[] numbers) {
        long lcm = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            lcm = lcm(lcm, numbers[i]);
        }
        return lcm;
    }

    public static long lcm(long a, long b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    public static long gcd(long a, long b) {
        while (b != 0) {
            long t = a;
            a = b;
            b = t % b;
        }
        return a;
    }
}
