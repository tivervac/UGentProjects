class Participant < ActiveRecord::Base
  belongs_to :event
  belongs_to :person
  self.primary_key = [:person_id, :event_id]
  validates :person_id, uniqueness: { scope: :event_id }
end
