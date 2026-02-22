interface Printable
begin
  procedure show();
end;

class Person implements Printable
begin
  var age: integer;

  constructor Create();
  begin
    age := 18;
  end;

  procedure show();
  begin
    writeln(age);
  end;
end;

var
  p: Person;

begin
   p := Person.Create();
  p.show();
end.
