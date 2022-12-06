def elves
  result = []
  current = 0
  File.readlines('./input.txt').each do |line|
    calories = line.to_i

    if calories.zero?
      result << current
      current = 0
    else
      current += calories
    end
  end

  result
end

puts elves.sort.reverse.take(3).sum
