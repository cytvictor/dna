To compile:
1. `ddlog -i test.dl`
2. `cd test_ddlog && cargo build --release`
3. `./target/release/test_cli`


```bash
start;
insert Node("A");
insert Node("B");
insert Node("C");
insert Node("D");
insert Edge("A", "B", 1);
insert Edge("A", "C", 4);
insert Edge("B", "C", 2);
insert Edge("B", "D", 5);
insert Edge("C", "D", 1);
commit;
dump;
```

Output:

```text
Shortest_path_cost:
Shortest_path_cost{.snode = "A", .dnode = "A", .int = 0, .path = []}
Shortest_path_cost{.snode = "A", .dnode = "B", .int = 1, .path = []}
Shortest_path_cost{.snode = "A", .dnode = "B", .int = 1, .path = ["B"]}
Shortest_path_cost{.snode = "A", .dnode = "C", .int = 3, .path = ["B"]}
Shortest_path_cost{.snode = "A", .dnode = "C", .int = 3, .path = ["C", "B"]}
Shortest_path_cost{.snode = "A", .dnode = "C", .int = 4, .path = []}
Shortest_path_cost{.snode = "A", .dnode = "C", .int = 4, .path = ["C"]}
Shortest_path_cost{.snode = "A", .dnode = "D", .int = 4, .path = ["C", "B"]}
Shortest_path_cost{.snode = "A", .dnode = "D", .int = 4, .path = ["D", "C", "B"]}
Shortest_path_cost{.snode = "A", .dnode = "D", .int = 5, .path = ["C"]}
Shortest_path_cost{.snode = "A", .dnode = "D", .int = 5, .path = ["D", "C"]}
Shortest_path_cost{.snode = "A", .dnode = "D", .int = 6, .path = ["B"]}
Shortest_path_cost{.snode = "A", .dnode = "D", .int = 6, .path = ["D", "B"]}
Shortest_path_cost{.snode = "B", .dnode = "B", .int = 0, .path = []}
Shortest_path_cost{.snode = "B", .dnode = "C", .int = 2, .path = []}
Shortest_path_cost{.snode = "B", .dnode = "C", .int = 2, .path = ["C"]}
Shortest_path_cost{.snode = "B", .dnode = "D", .int = 3, .path = ["C"]}
Shortest_path_cost{.snode = "B", .dnode = "D", .int = 3, .path = ["D", "C"]}
Shortest_path_cost{.snode = "B", .dnode = "D", .int = 5, .path = []}
Shortest_path_cost{.snode = "B", .dnode = "D", .int = 5, .path = ["D"]}
Shortest_path_cost{.snode = "C", .dnode = "C", .int = 0, .path = []}
Shortest_path_cost{.snode = "C", .dnode = "D", .int = 1, .path = []}
Shortest_path_cost{.snode = "C", .dnode = "D", .int = 1, .path = ["D"]}
Shortest_path_cost{.snode = "D", .dnode = "D", .int = 0, .path = []}

>> exit;
```