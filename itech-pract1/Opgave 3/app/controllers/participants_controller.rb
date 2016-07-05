class ParticipantsController < ApplicationController
  def new
  end

  def create
    @event = Event.find(params[:event_id])

    respond_to do |format|
      if !params.has_key?(:participant)
        format.html { redirect_to event_path(@event) }
        format.json { render json: @event.errors, status: :bad_request }
      else
        @participant = @event.participants.create(params[:participant].permit(:person_id))

        format.html { redirect_to event_path(@event) }
        format.json { render json: event_url(@event), status: :created }
      end
    end
  end

  def destroy
    @event = Event.find(params[:event_id])

    @event.people.delete(params[:id])

    respond_to do |format|
      format.html { redirect_to @event }
      format.json { render json: event_url(@event), status: :accepted }
    end
  end
end
