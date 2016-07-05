json.(@event, :id, :title, :text, :start, :end, :created_at, :updated_at)

json.participants @event.participants do |json, participant|
	json.(participant, :person_id)
	json.person person_url(participant.person_id)
end

json.comments @event.comments do |json, comment|
	json.(comment, :id, :body, :created_at, :updated_at)
	json.commenter person_url(comment.person.id)
end