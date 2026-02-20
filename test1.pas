class Person
begin
  var age: integer;
  constructor Create();
  begin
    age := 18;
  end;
end;

var
  p: Person;

begin
  p := Person.Create();
  writeln(p.age);
end.
