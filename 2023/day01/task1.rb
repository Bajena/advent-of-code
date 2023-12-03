class FileReader
  def initialize(file_path = "input.txt")
    @file_path = file_path
  end

  def lines
    File.readlines(@file_path)
  end
end

class Task1
  def run
    FileReader.new("input1.txt").lines.sum do |line|
      numbers = line.split('').select { |char| number?(char) }
      [numbers.first, numbers.last].join.to_i
    end
  end

  def number?(string)
    true if Integer(string) rescue false
  end
end

print Task1.new.run
