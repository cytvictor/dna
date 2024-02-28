To compile:
1. `ddlog -i test.dl`
2. `cd test_ddlog && cargo build --release`
3. `./target/release/test_cli`


```bash
>> start;
>> 
>> insert Node("A");
>> insert Node("B");
>> insert Node("C");
>> insert Node("D");
>> insert Edge("A", "B", 1);
>> insert Edge("A", "C", 4);
>> insert Edge("B", "C", 2);
>> insert Edge("B", "D", 5);
>> insert Edge("C", "D", 1);
>> commit;
>> dump;
Shortest_path:
Shortest_path{.snode = "A", .dnode = "A", .int = 0}
Shortest_path{.snode = "A", .dnode = "B", .int = 1}
Shortest_path{.snode = "A", .dnode = "C", .int = 3}
Shortest_path{.snode = "A", .dnode = "C", .int = 4}
Shortest_path{.snode = "A", .dnode = "D", .int = 4}
Shortest_path{.snode = "A", .dnode = "D", .int = 5}
Shortest_path{.snode = "A", .dnode = "D", .int = 6}
Shortest_path{.snode = "B", .dnode = "B", .int = 0}
Shortest_path{.snode = "B", .dnode = "C", .int = 2}
Shortest_path{.snode = "B", .dnode = "D", .int = 3}
Shortest_path{.snode = "B", .dnode = "D", .int = 5}
Shortest_path{.snode = "C", .dnode = "C", .int = 0}
Shortest_path{.snode = "C", .dnode = "D", .int = 1}
Shortest_path{.snode = "D", .dnode = "D", .int = 0}

>> exit;
```