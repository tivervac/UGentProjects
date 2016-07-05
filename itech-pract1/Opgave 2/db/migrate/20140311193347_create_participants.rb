class CreateParticipants < ActiveRecord::Migration
  def change
    create_join_table :events, :people, table_name: :participants do |t|
      t.column :person_id, :integer
      t.column :event_id, :integer
      t.timestamps
    end
    add_index :participants, [:person_id, :event_id]
  end
end
