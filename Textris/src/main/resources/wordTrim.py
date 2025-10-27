import string

read_file = open("wordsRaw.txt","r")
out_file = open("Twordlist.txt","w")

for line in read_file:

    line = line.strip()
    line_l = len(line)
    
    if line_l < 3 or line_l > 5:
        continue

    # Purge lines with non-letter characters
    if any(not char.isalpha() for char in line):
        continue

    # Purge names, acronyms, concatenations
    if any(char.isupper() for char in line):
        continue

    print(line,file=out_file)

read_file.close()
out_file.close()

print("Done.")
