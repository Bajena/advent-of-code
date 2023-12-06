require 'debug'

class FileReader
  def initialize(file_path = "input.txt")
    @file_path = file_path
  end

  def lines
    File.readlines(@file_path)
  end
end

class PartNumber
  def initialize(start_x, end_x, y, number)
    @start_x = start_x
    @end_x = end_x
    @y = y
    @number = number
  end

  def add_digit(digit)
    @number = (@number.to_s + digit.to_s).to_i
    @end_x += 1
  end

  attr_accessor :start_x, :end_x, :y, :number
end

class SchemaSymbol
  def initialize(x, y, symbol)
    @x = x
    @y = y
    @symbol = symbol
  end

  attr_accessor :x, :y, :symbol
end

class Task
  def run
    part_numbers = []
    symbols = []

    FileReader.new.lines.each.with_index do |line, y|
      current_part_number = nil

      line.split('').reject { |c| c == "\n" }.each.with_index do |char, x|
        if number?(char)
          if current_part_number.nil?
            current_part_number = PartNumber.new(x, x, y, char.to_i)
            part_numbers << current_part_number
          else
            current_part_number.add_digit(char.to_i)
          end
        elsif char == "."
          current_part_number = nil
        else
          symbols << SchemaSymbol.new(x, y, char)
          current_part_number = nil
        end
      end
    end

    valid_parts = part_numbers.select do |part_number|
      symbols.any? do |symbol|
        symbol.x.between?(part_number.start_x - 1, part_number.end_x + 1) && symbol.y.between?(part_number.y - 1, part_number.y + 1)
      end
    end

    puts valid_parts.map(&:number).sum
  end

  def number?(string)
    true if Integer(string) rescue false
  end
end

Task.new.run
