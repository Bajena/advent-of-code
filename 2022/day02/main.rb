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

result = 0

File.readlines('input.txt').each.with_index do |line, i|
  opponent_symbol = line[0]
  player_symbol = line[2]

  gp = game_points(opponent_symbol, player_symbol)

  result += gp
end

puts result
