use std::fs::File;
use std::io::{self, BufRead};
use std::path::Path;

fn main() {
    part2();
}

fn part1() {
    let mut current_elf = 0;
    let mut max_elf = 0;

    if let Ok(lines) = read_lines("./input.txt") {
        // Consumes the iterator, returns an (Optional) String
        for line in lines {
            if let Ok(calories_string) = line {
                match calories_string.parse::<i32>() {
                    Ok(n) => current_elf += n,
                    Err(_e) => {
                        if current_elf > max_elf {
                            max_elf = current_elf;
                        }
                        current_elf = 0;
                    },
                }
            }
        }
    }

    println!("Max elf:{}", max_elf);
}

fn part2() {
    let mut current_elf = 0;
    let mut elves: Vec<i32> = Vec::new();

    if let Ok(lines) = read_lines("./input.txt") {
        // Consumes the iterator, returns an (Optional) String
        for line in lines {
            if let Ok(calories_string) = line {
                match calories_string.parse::<i32>() {
                    Ok(n) => current_elf += n,
                    Err(_e) => {
                        elves.push(current_elf);
                        current_elf = 0;
                    },
                }
            }
        }
    }

    println!("Elves:{:?}", elves.sort());
}
// The output is wrapped in a Result to allow matching on errors
// Returns an Iterator to the Reader of the lines of the file.
fn read_lines<P>(filename: P) -> io::Result<io::Lines<io::BufReader<File>>>
where P: AsRef<Path>, {
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}
