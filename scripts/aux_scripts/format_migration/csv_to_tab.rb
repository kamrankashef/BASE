#!/usr/bin/ruby
#
#
# Convert data files: Hana -> SQL Server
# TODO Change M9734 and MARK48 to something harder to collide with and/or include some kind of checking

export_home = ARGV[0]
out_dir = ARGV[1]

`rm -rf #{out_dir}`
`mkdir #{out_dir}`

files = `find "#{export_home}"|grep data.csv`.split("\n")

# For each data file in #{export_home}
#   Convert data.csv -> <table_name>.tab
#   Echo out import command
files.each do | file |
  
   table = `dirname "#{file}"`[/[^ \/]+$/].gsub("\n","")
   if table == "MSG_GamePlayByPlay" then
       puts "-- Skipping #{table} to handle below"
       next
   end
   `sed -e 's/\\\\,/M9734/g' "#{file}" | sed -e 's/,/'$'\t''/g' -e 's/\"//g' | sed -e 's/M9734/,/g' > "#{out_dir}/#{table}.tab"`

   insert=<<mark
BULK INSERT #{table}

FROM 'C:\\#{table}.tab'
WITH
(
  FIRSTROW = 1,
  FIELDTERMINATOR = '\\t',
  ROWTERMINATOR = '0x0a',
  TABLOCK
);

mark
    puts "DELETE FROM #{table};"
    puts insert
end 

# Handle data conversion for MSG_GamePlayByPlay
#   Convert component CSVs -> component TABs
#   Echo out import command
puts "DELETE FROM MSG_GamePlayByPlay;"

files = `find "#{export_home}/MSG_GamePlayByPlay"|grep csv`.split("\n")

files.each do | file |
   table = `basename "#{file}"`[/[^ \/]+$/].gsub(".csv", "").gsub("\n","")
    command=<<mark
cat "#{file}" | sed -E 's/[^,]+,//' | sed ':a;s/^\\(\\([^"]*,\\?\\|"[^",]*",\\?\\)*"[^",]*\\),/\\1MARK48/;ta' |sed -e 's/MARK48/\\\\,/g' |sed -e 's/\\\\,/M9734/g'  | sed -e 's/,/'$'\\t''/g' -e 's/\"//g' | sed -e 's/M9734/,/g' | sed -e 's/\\tNA/\\t/g' > "#{out_dir}/#{table}.tab"
mark
    `#{command}`
insert=<<mark
BULK INSERT MSG_GamePlayByPlay
FROM 'C:\\MSG_GamePlayByPlay\\#{table}.tab'
WITH
(
  FIRSTROW = 2,
  FIELDTERMINATOR = '\\t',
  ROWTERMINATOR = '0x0a',
  TABLOCK
);
mark

puts insert

end

