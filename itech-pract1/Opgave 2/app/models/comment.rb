class Comment < ActiveRecord::Base
  belongs_to :person
  belongs_to :event

  validates :person_id, :event_id, :body, presence: true
end
