class Counter
begin
  var value: integer;

  constructor Create();
  begin
    value := 7;
  end;

  procedure show();
  begin
    writeln(value);
  end;
end;

var c: Counter;

begin
  c := Counter.Create();
  c.show();
end.
