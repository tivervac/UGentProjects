class ParticipantsController < ApplicationController
  def new
  end

  def create
    @event = Event.find(params[:event_id])
    
    if !params.has_key?(:participant)
      redirect_to event_path(@event)
    else    
      @participant = @event.participants.create(params[:participant].permit(:person_id))
    
      redirect_to event_path(@event)
    end
  end

   def destroy
     @event = Event.find(params[:event_id])

     @event.people.delete(params[:id])

     redirect_to @event
  end
end
