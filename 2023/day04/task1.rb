require 'debug'

class FileReader
  def initialize(file_path = "input.txt")
    @file_path = file_path
  end

  def lines
    File.readlines(@file_path)
  end
end

class Card
  def initialize(my_numbers, winning_numbers)
    @my_numbers = my_numbers
    @winning_numbers = winning_numbers
  end

  def self.build(line)
    numbers = line.split(": ").last

    my_numbers = numbers.split(" | ").first.split(" ").map(&:to_i)
    winning_numbers = numbers.split(" | ").last.split(" ").map(&:to_i)

    Card.new(my_numbers, winning_numbers)
  end

  attr_reader :my_numbers, :winning_numbers

  def matching_numbers_count
    @matching_numbers ||= begin
      (@my_numbers & @winning_numbers).length
    end
  end

  def points
    return @points if @points

    @points = if matching_numbers_count == 0
      0
    else
      2**(matching_numbers_count - 1)
    end
  end
end

class Task
  def run
    cards = FileReader.new.lines.map do |line|
      Card.build(line)
    end

    card_counts = {}

    cards.each.with_index do |card, i|
      i = cards.length - 1 - i
      card = cards[i]

      card_counts[i] = 1

      puts "Card #{i+1} has #{card.matching_numbers_count} matching numbers"

      next if card.matching_numbers_count == 0

      ((i+1)...(i+1+card.matching_numbers_count)).each do |won_card_index|
        card_counts[i] += (card_counts.key?(won_card_index) ? card_counts[won_card_index] : 0)
      end
    end

    puts card_counts


    puts card_counts.values.sum
  end
end

Task.new.run
