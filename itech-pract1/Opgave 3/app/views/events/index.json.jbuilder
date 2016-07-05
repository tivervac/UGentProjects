json.(@events) do |json, event|
  json.(event, :id, :title, :text, :start, :end)
  json.event event_url(event)
end