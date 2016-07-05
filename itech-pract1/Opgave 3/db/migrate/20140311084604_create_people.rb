class CreatePeople < ActiveRecord::Migration
  def change
    create_table :people do |t|
      t.string :name
      t.string :email
      t.date :dateofbirth

      t.timestamps
    end
  end
end
