let sample_numbers = [12; 4; 7; 4; 9; 12; 3; 12; 4; 8]

let mean numbers =
  let sum = List.fold_left ( +. ) 0.0 (List.map float_of_int numbers) in
  sum /. float_of_int (List.length numbers)

let median sorted_numbers =
  let count = List.length sorted_numbers in
  let middle = count / 2 in
  if count mod 2 = 1 then
    float_of_int (List.nth sorted_numbers middle)
  else
    let left = float_of_int (List.nth sorted_numbers (middle - 1)) in
    let right = float_of_int (List.nth sorted_numbers middle) in
    (left +. right) /. 2.0

let frequencies sorted_numbers =
  let add_number groups number =
    match groups with
    | (value, count) :: rest when value = number -> (value, count + 1) :: rest
    | _ -> (number, 1) :: groups
  in
  List.rev (List.fold_left add_number [] sorted_numbers)

let modes sorted_numbers =
  let counts = frequencies sorted_numbers in
  let highest_frequency =
    List.fold_left (fun highest (_, count) -> max highest count) 0 counts
  in
  counts
  |> List.filter (fun (_, count) -> count = highest_frequency)
  |> List.map fst

let parse_integer text =
  try int_of_string text with
  | Failure _ ->
      Printf.eprintf "Error: '%s' is not a valid integer.\n" text;
      exit 1

let string_of_list numbers =
  numbers
  |> List.map string_of_int
  |> String.concat ", "
  |> Printf.sprintf "[%s]"

let print_results numbers =
  let sorted_numbers = List.sort compare numbers in
  Printf.printf "STATISTICS CALCULATOR - OCAML (FUNCTIONAL)\n";
  Printf.printf "=========================================\n";
  Printf.printf "Input:  %s\n" (string_of_list numbers);
  Printf.printf "Sorted: %s\n" (string_of_list sorted_numbers);
  Printf.printf "Mean:   %.2f\n" (mean numbers);
  Printf.printf "Median: %.2f\n" (median sorted_numbers);
  Printf.printf "Mode(s): %s\n" (string_of_list (modes sorted_numbers))

let () =
  let arguments = Array.to_list Sys.argv |> List.tl in
  let numbers =
    match arguments with
    | [] -> sample_numbers
    | values -> List.map parse_integer values
  in
  print_results numbers
