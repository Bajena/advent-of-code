require 'debug'

class FileReader
  def initialize(file_path = "input.txt")
    @file_path = file_path
  end

  def lines
    File.readlines(@file_path)
  end
end

class SetOfCubes
  def initialize(configurations)
    raise ArgumentError unless configurations.is_a?(Array)
    raise ArgumentError unless configurations.all? { |c| c.is_a?(Configuration) }

    @configurations = configurations
  end

  def update(color, count)
    config = find_configuration(color)
    if config
      config.count = count
    else
      configurations << Configuration.new(color, count)
    end
  end

  def configurations
    @configurations
  end

  def find_configuration(color)
    configurations.find { |c| c.color == color }
  end

  def contains?(other_set)
    other_set.configurations.each do |other_config|
      config = configurations.find { |c| c.color == other_config.color }
      return false unless config
      return false if config.count < other_config.count
    end

    true
  end
end

class Configuration
  COLORS = %w[red green blue].freeze

  def initialize(color, count)
    raise ArgumentError unless COLORS.include?(color)
    raise ArgumentError unless count.is_a?(Integer)
    raise ArgumentError unless count.positive?

    @color = color
    @count = count
  end

  def color
    @color
  end

  def count
    @count
  end

  def count=(count)
    @count = count
  end
end

class Game
  def initialize(id, sets)
    @id = id
    @sets = sets
  end

  def possible_with?(set_of_cubes)
    GamePossibilityChecker.new(self, set_of_cubes).possible?
  end

  def id
    @id
  end

  def sets
    @sets
  end
end

class GameParser
  def initialize(line)
    @line = line
  end

  def game
    Game.new(id, sets)
  end

  private

  def id
    @id ||= @line.split('Game ').last.split(':').first.to_i
  end

  def sets
    @sets ||= @line.split(': ').last.split('; ').map do |set|
      SetOfCubes.new(configurations(set))
    end
  end

  def configurations(set)
    set.split(', ').map do |configuration|
      count, color = configuration.split(' ')
      Configuration.new(color, count.to_i)
    end
  end
end

class GamePossibilityChecker
  def initialize(game, set_of_cubes)
    @game = game
    @set_of_cubes = set_of_cubes
  end

  def possible?
    @game.sets.all? do |set|
      @set_of_cubes.contains?(set)
    end
  end
end

class MinSetFinder
  def initialize(game)
    @game = game
  end

  def find
    min_set = SetOfCubes.new([])

    @game.sets.each do |set|
      set.configurations.each do |config|
        min_set.update(config.color, config.count) if config.count > min_set.find_configuration(config.color)&.count.to_i
      end
    end

    min_set
  end
end

class Task
  def run
    games = FileReader.new.lines.map do |line|
      GameParser.new(line).game
    end

    min_sets = games.map do |game|
      MinSetFinder.new(game).find
    end

    result = min_sets.sum do |min_set|
      power = 1
      min_set.configurations.each do |config|
        power *= config.count
      end

      power
    end

    puts result
  end
end

Task.new.run
