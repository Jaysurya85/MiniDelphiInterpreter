class Person
begin
  var age: integer;

  constructor Create();
  begin
    age := 18;
  end;

  destructor Destroy();
  begin
    writeln(age);
  end;
end;

var p: Person;

begin
  p := Person.Create();
  p.Destroy();
end.
