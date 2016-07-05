json.(@people) do |json, person|
  json.(person, :id, :name, :email, :dateofbirth)
  json.person person_url(person)
end
