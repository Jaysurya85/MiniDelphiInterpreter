class Person
begin
  var age: integer;

  constructor Create();
  begin
    age := 18;
  end;
end;

class Student extends Person
begin
  var roll: integer;

  constructor Create();
  begin
    roll := 10;
  end;
end;

var s: Student;

begin
  s := Student.Create();
  writeln(s.age);
  writeln(s.roll);
end.
