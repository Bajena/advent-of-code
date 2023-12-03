class FileReader
  def initialize(file_path = "input.txt")
    @file_path = file_path
  end

  def lines
    File.readlines(@file_path)
  end
end


class Task2
  WORDS_TO_NUMBERS = {
    "one" => 1,
    "two" => 2,
    "three" => 3,
    "four" => 4,
    "five" => 5,
    "six" => 6,
    "seven" => 7,
    "eight" => 8,
    "nine" => 9
  }

  def run
    FileReader.new("input2.txt").lines.sum do |line|
      [find_first_number(line), find_last_number(line)].join.to_i
    end
  end

  def find_first_number(line)
    i = 0

    while i < line.length
      number = number_at_index(line, i)
      return number if number

      i += 1
    end
  end

  def find_last_number(line)
    i = line.length - 1

    while i >= 0
      number = number_at_index(line, i)
      return number if number

      i -= 1
    end
  end

  def number_at_index(line, i)
    return line[i] if number?(line[i])

    WORDS_TO_NUMBERS.find do |word, number|
      return number if line[i...i + word.length] == word
    end

    nil
  end

  def number?(character)
    true if Integer(character) rescue false
  end
end

print Task2.new.run
