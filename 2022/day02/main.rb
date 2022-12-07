ROCK = 'X'
PAPER = 'Y'
SCISSORS = 'Z'

OPPONENT_ROCK = 'A'
OPPONENT_PAPER = 'B'
OPPONENT_SCISSORS = 'C'

SYMBOL_POINTS = {
  ROCK => 1,
  PAPER => 2,
  SCISSORS => 3
}

WIN_POINTS = 6
DRAW_POINTS = 3
LOSE_POINTS = 0

LOSE_SYMBOL = 'X'
DRAW_SYMBOL = 'Y'
WIN_SYMBOL = 'Z'

def draw?(opponent_symbol, player_symbol)
  opponent_symbol == OPPONENT_ROCK && player_symbol == ROCK ||
    opponent_symbol == OPPONENT_PAPER && player_symbol == PAPER ||
    opponent_symbol == OPPONENT_SCISSORS && player_symbol == SCISSORS
end

def game_points(opponent_symbol, player_symbol)
  symbol_points = SYMBOL_POINTS[player_symbol]

  return DRAW_POINTS + symbol_points if draw?(opponent_symbol, player_symbol)

  is_win =
    case player_symbol
    when ROCK then opponent_symbol == OPPONENT_SCISSORS
    when PAPER then opponent_symbol == OPPONENT_ROCK
    when SCISSORS then opponent_symbol == OPPONENT_PAPER
    else raise "unknown symbol: #{player_symbol}"
    end

  symbol_points + (is_win ? WIN_POINTS : LOSE_POINTS)
end

def opponent_to_player_symbol(opponent_symbol)
  case opponent_symbol
  when OPPONENT_ROCK then ROCK
  when OPPONENT_PAPER then PAPER
  when OPPONENT_SCISSORS then SCISSORS
  else raise 'wrong symbol'
  end
end

def player_symbol_for_result(opponent_symbol, result_symbol)
  return opponent_to_player_symbol(opponent_symbol) if result_symbol == DRAW_SYMBOL

  is_win = result_symbol == WIN_SYMBOL

  case opponent_symbol
  when OPPONENT_ROCK then is_win ? PAPER : SCISSORS
  when OPPONENT_PAPER then is_win ? SCISSORS : ROCK
  when OPPONENT_SCISSORS then is_win ? ROCK : PAPER
  else raise "unknown symbol: #{opponent_symbol}"
  end
end

def part1
  result = 0

  File.readlines('input.txt').each.with_index do |line, i|
    opponent_symbol = line[0]
    player_symbol = line[2]

    gp = game_points(opponent_symbol, player_symbol)

    result += gp
  end

  puts result
end

def part2
  result = 0

  File.readlines('input.txt').each.with_index do |line, i|
    opponent_symbol = line[0]
    player_symbol = player_symbol_for_result(opponent_symbol, line[2])

    gp = game_points(opponent_symbol, player_symbol)

    result += gp
  end

  puts result
end

part2
