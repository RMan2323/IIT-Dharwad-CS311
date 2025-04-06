// package processor;

// import generic.Element;
// import generic.Event;
// import generic.Event.EventType;
// import generic.ArithmeticEvent;
// import generic.ArithmeticEvent.OperationType;
// import generic.ExecutionCompleteEvent;
// import generic.Simulator;

// public class ALU implements Element {
//     @Override
//     public void handleEvent(Event e) {
//         ArithmeticEvent event = (ArithmeticEvent) e;
//         if(event.operation == OperationType.mul){
//             Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime(), this, event.getRequestingElement(), event.rd, event.op1*event.op2, false, 0));
//         }
//         else if(event.operation == OperationType.div){
//             Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime(), this, event.getRequestingElement(), event.rd, event.op1/event.op2, true, event.op1%event.op2));
//         }else{
//             Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(Clock.getCurrentTime(), this, event.getRequestingElement(), event.rd, event.op1*event.op2, false, 0));
//         }
//     }
// }
