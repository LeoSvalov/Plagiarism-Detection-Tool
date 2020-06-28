# need to install package requests
# path need to contain '/' before and after student name

import requests
import os.path

students = []
jplag_students = []
all_jplag_students = []
all_students = []
moss_students = []
threshold = 0  # set threshold
# TODO add to readme place to add threshold


class Student:  # Class to store students
    name = ""
    plagiarism = 0
    link = ""

    def __init__(self, n, pl, lk):
        self.name = n
        self.plagiarism = pl
        self.link = lk


def new_values(student1, student2):
    if student1.plagiarism > student2.plagiarism:
        return Student(student1.name, student1.plagiarism, student1.link)
    return Student(student2.name, student2.plagiarism, student2.link)


def add_information(local_name, plg, local_link, checker):  # this function adds student to unique list of moss or jplag
    students_size = 0
    if checker == "moss":
        students_size = len(students)
    elif checker == "jplag":
        students_size = len(jplag_students)
    student_exists = -1
    for index in range(0, students_size):
        if checker == "moss":
            if students[index].name == local_name:
                student_exists = index
                break
        if checker == "jplag":
            if jplag_students[index].name == local_name:
                student_exists = index
                break
    if student_exists == -1:
        if checker == "moss":
            students.append(Student(local_name, plg, local_link))
        if checker == "jplag":
            jplag_students.append(Student(local_name, plg, local_link))
    else:
        if checker == "moss":
            students[student_exists] = new_values(students[student_exists], Student(local_name, plg, local_link))
        if checker == "jplag":
            jplag_students[student_exists] = new_values(jplag_students[student_exists],
                                                        Student(local_name, plg, local_link))


short_report = open("result/report", "rt")
moss_url = short_report.readline()  # getting moss report url

if moss_url[-1] == '\\n':
    moss_url = moss_url[:-1]  # removing '\n'
moss_report_str = str(requests.get(moss_url).content)  # getting source code of page
moss_report = moss_report_str.split("\\n")
print("Page downloaded\nParsing...")

size_moss_report = len(moss_report)
for el in range(14, size_moss_report - 5):  # Parsing Moss report source code to store all plagiarism detections
    if (el - 14) % 3 == 2:
        continue
    moss_report[el] = moss_report[el][17:-4]
    buf = moss_report[el].split("\"")
    el_link = buf[0]
    buf1 = moss_report[el].split(" ")
    el_plag = int(buf1[1][1:-2])
    buf2 = moss_report[el].split("/")
    el_name = buf2[-2]
    #  print(el_name + " " + el_link + " " + str(el_plag) + " %") # All parsed results of students
    add_information(el_name, el_plag, el_link, "moss")  # Making unique (the maximum one) plagiarism percentage and link
    moss_students.append([el_name, el_plag, el_link])
for i in range(0, len(moss_students), 2):  # all students store [name1 name2 percentage link] for moss report
    all_students.append([moss_students[i][0], moss_students[i + 1][0], moss_students[i][1], moss_students[i][2]])
    all_students.append([moss_students[i + 1][0], moss_students[i][0], moss_students[i + 1][1], moss_students[i][2]])

print("Parsed successfully\nUnique Students:")
for a in students:
    print(a.name + " " + a.link + " " + str(a.plagiarism) + " %")

#  Parsing MOSS Completed
#  Parsing Jplag

print("\nParsing Jplag report")
buf = short_report.readline()

for i in range(100000):  # Parsing all "match" files from Jplag report
    if not os.path.exists("result/match" + str(i) + "-top.html"):
        break
    jplag_file = open("result/match" + str(i) + "-top.html")
    local_list = jplag_file.read().split("\n")[18].split("<TH>")
    # print(local_list[2].split(" ")[0] + " " + local_list[2].split(" ")[1][1:-2] + " " + "result/match" + str(i))
    # Parsing Jplag report files to store all plagiarism detections
    add_information(local_list[2].split(" ")[0], local_list[2].split(" ")[1][1:-2], "result/match" + str(i) + ".html",
                    "jplag")
    add_information(local_list[3].split(" ")[0], local_list[3].split(" ")[1][1:-2], "result/match" + str(i) + ".html",
                    "jplag")
    # all jplag students store [name1 name2 percentage link]
    all_jplag_students.append([local_list[2].split(" ")[0], local_list[3].split(" ")[0],
                               local_list[2].split(" ")[1][1:-2], "result/match" + str(i) + ".html"])
    all_jplag_students.append([local_list[3].split(" ")[0], local_list[2].split(" ")[0],
                               local_list[3].split(" ")[1][1:-2], "result/match" + str(i) + ".html"])

print("Parsed successfully\nUnique Students(Jplag):")
for a in jplag_students:
    print(a.name + " " + a.link + " " + str(a.plagiarism) + " %")

#  Parsed Jplag
# Parsing Junit tests
junit_students = []
junit = open("Junit-5-validation/log.txt")
junit_lines = list(junit)
junit_n = len(junit_lines)
index = 0
#  print(junit_lines)
while index < junit_n:
    if junit_lines[index] == "\n":
        index += 1
        continue
    if index == 0 or junit_lines[index].split()[1] == "log:":
        loc_list = [junit_lines[index].split()[0]]
        index += 1
        while index < junit_n:
            if junit_lines[index] == "\n":
                break
            loc_list.append(junit_lines[index])
            index += 1
        junit_students.append(loc_list)
    else:
        index += 1

# Parsed Junit tests
#  Creating report

print("Generating report")
report = open("report.html", "wt")  # Creating report file
report.write('''<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8" />
  <title>Plagiarism Report</title>
 </head>
 <body>
 <a href="''' + moss_url + '''
 "> MOSS report </a>
</p>
<a href="''' + "result/index.html" + '''"> Jplag report </a>
</p><hr>
<p>Most percentage of plagiarism per student</p>
 <table border="2">
   <tr><th>Name of student:</th><th>Similarity percentage determined using MOSS:</th><th>Similarity percentage determined using Jplag:</th></tr>
''')

names_of_students = []  # Storing all students who plagiarised
for el in students:
    names_of_students.append(el.name)
names_of_students.sort()  # Sorting them
for el in names_of_students:  # Creating table with sorted students in Report
    for a in students:
        if a.name == el:
            report.write(
                "<tr><td>" + a.name + "</td><td><a href=\"" + a.link + "\">" + str(a.plagiarism) + " %</a></td>")
            break
    for b in jplag_students:
        if b.name == el:
            report.write("<td><a href=\"" + b.link + "\">" + str(b.plagiarism) + " %</a></td></tr>\n")

report.write('''</table><hr>''')


def all_plag_write(list, checker):  # This function create matrix for all pairs of students with grayscale
    report.write(                   # unstead number persentage
        '''<p>Comparing all works of students for plagiarism ({} checker)</p><table border="2"><tr><th>Name:</th>'''.format(
            checker))
    for index in names_of_students:
        report.write("<th>{}</th>".format(index))
    report.write("</tr>\n")
    for index in names_of_students:  # Creating table with sorted students in Report
        report.write("<tr><td>{}</td>".format(index))
        for element in names_of_students:
            if index == element:
                report.write("<td bgcolor=\"#ffffff\" align=\"center\"></td>")
            for c in list:  # all students store [name1 name2 percentage link] for moss report
                if c[0] == element and c[1] == index:
                    if str(c[2]).find('.') == 2 or str(c[2]).find('.') == 3:
                        c[2] = c[2][:-2:]
                    color = 254 - int(int(c[2]) * 2.5)
                    color_str = str(hex(color)) * 3
                    report.write(
                        "<td bgcolor=\"#{}\" align=\"center\"><a href=\"{}\">______</a></td>".format(color_str, c[3]))
                    break
    report.write('''</tr></table>''')


report.write('''<p>White = 0 % plagiarism<br>Black = 100% plagiarism</p>''')
all_plag_write(all_students, "MOSS")
all_plag_write(all_jplag_students, "Jplag")

report.write(''' </body>
</html>
''')  # Report created
single_report = open("Report for each student.html", "wt")  # Opening new file to write report
single_report.write('''<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8" />
  <title>Plagiarism Report</title>
  <style type="text/css">
   red {  
    color: #ff0000;
   }
   green {
    color: #00ff00;
   }
   </style>
 </head>
 <body>
''')


def write_single_student_report(given_list):  # Creating function to make less code
    plagiarism_list = []  # Write them each in new paragraph and sorted by Moss plagiarism persentage
    for b in given_list:
        if b[0] == a:
            plagiarism_list.append(b[2])
    plagiarism_list.sort(reverse=1)
    for b in plagiarism_list:
        for c in given_list:
            if c[2] == b and c[0] == a:
                if str(b).find('.') == 2 or str(b).find('.') == 3:
                    b = b[:-2:]
                if int(b) >= threshold:
                    single_report.write('<p>{} <a href="{}">{} %</a></p> '.format(c[1], c[3], c[2]))


for a in names_of_students:
    single_report.write('<h3>{}</h3><p>Plagiarism detected comparing to all students:</p><p>Moss report:</p>'.format(a))
    write_single_student_report(all_students)
    single_report.write('<br><p>Jplag report:</p>')
    write_single_student_report(all_jplag_students)
    single_report.write("\n<br>")

    # Including Junit report into main report
    for b in junit_students:
        if b[0] == a:
            single_report.write("\n<p>Junit tests:</p>")
            loc_size = len(b)
            for c in range(1, loc_size):
                if b[c][:1:] == '-':
                    single_report.write("<p>[<green>Done</green>]{}</p>".format(b[c][2:-2:]))
                else:
                    single_report.write("<p>[<red>No</red>]{}</p>".format(b[c][2:-2:]))
    single_report.write('<hr>\n')

single_report.write('''</body>
</html>''')  # Report created

# Second report by student
report_by_student = open("Report by student.html", "wt") # opening 2 report with separate student
report_by_student.write('''<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8" />
  <title>Plagiarism Report</title>
  <style type="text/css">
   red {  
    color: #ff0000;
   }
   green {
    color: #00ff00;
   }
   </style>
 </head>
 <body>
''')

for a in names_of_students:
    report_by_student.write('<h3>{}</h3><p>Plagiarism detected comparing to all students:</p>'.format(a))
    plag_list = []  # Let's sort studnets by percentage of plagiarism of MOSS
    table_list = [[], [], []]  # make list to fill table easier in future
    for b in all_students:
        if b[0] == a:
            plag_list.append(b[2])
            table_list[0].append(b[1])
            table_list[1].append([b[2], b[3]])
    plag_list.sort(reverse=1)  # sort students by percentage of plagiarism of MOSS
    for f in table_list[0]:
        for c in jplag_students:
            if c.name == f:
                table_list[2].append([c.plagiarism, c.link])
                break

    report_by_student.write('<table border="2">\n<tr><th>Name:</th>')  # Writing table to file
    for b in table_list[0]:
        report_by_student.write('<th>{}</th>'.format(b))
    report_by_student.write('</tr>\n<tr><td>MOSS result:</td>')
    for b in table_list[1]:
        report_by_student.write('<td><a href=\"{}\">{} %</td>'.format(b[1], b[0]))
    report_by_student.write('</tr>\n<tr><td>Jplag result:</td>')
    for b in table_list[2]:
        report_by_student.write('<td><a href=\"{}\">{} %</td>'.format(b[1], b[0]))
    report_by_student.write('</tr>\n</table>')
    report_by_student.write("\n<br>")

    # Making Jplag report
    for b in junit_students:
        if b[0] == a:
            report_by_student.write("\n<p>Junit tests:</p>")
            loc_size = len(b)
            for c in range(1, loc_size):
                if b[c][:1:] == '-':
                    report_by_student.write("<p>[<green>Done</green>]{}</p>".format(b[c][2:-2:]))
                else:
                    report_by_student.write("<p>[<red>No</red>]{}</p>".format(b[c][2:-2:]))
    report_by_student.write('<hr>\n')

report_by_student.write('''</body>
</html>''')  # Report created

# End of second report by student
print("Report generated")
