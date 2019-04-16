/*
 * *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import services.SystemConfigurationService;
import domain.Actor;
import domain.Message;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	MessageService				messageService;

	@Autowired
	ActorService				actorService;

	@Autowired
	SystemConfigurationService	systemConfigurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Message> messages;

		messages = this.messageService.findMessagesOrderByTagByActorLogged();

		result = new ModelAndView("message/list");

		result.addObject("messages", messages);
		result.addObject("requestURI", "message/list.do");

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int messageId) {
		ModelAndView result;
		Message messageEntity = null;

		try {
			messageEntity = this.messageService.findMessageActorLogged(messageId);
			result = new ModelAndView("message/show");
			result.addObject("messageEntity", messageEntity);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(messageEntity, "hacking.logged.error");
			else
				result = this.createEditModelAndView(messageEntity, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int messageId) {
		ModelAndView result;
		Message messageEntity = null;

		try {
			messageEntity = this.messageService.findMessageActorLogged(messageId);
			this.messageService.delete(messageEntity);
			result = new ModelAndView("redirect:/message/list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(messageEntity, "hacking.logged.error");
			else
				result = this.createEditModelAndView(messageEntity, "commit.error");

		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createMessage(@RequestParam(required = false) final String broadcast) {
		ModelAndView result;
		Message messageEntity;

		messageEntity = this.messageService.create();

		result = this.createEditModelAndView(messageEntity);
		if (broadcast != null)
			result.addObject("broadcast", broadcast);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView createMessage(@ModelAttribute("messageEntity") Message messageEntity, final BindingResult binding, @RequestParam(required = false) final String broadcast) {
		ModelAndView result;

		try {
			if (broadcast != null) {
				final Collection<Actor> actorsSystem = this.actorService.findAllActorsExceptLogged();
				messageEntity.setRecipients(actorsSystem);
				if (messageEntity.getRecipients().size() == 0) {
					final Actor system = this.actorService.getSystemActor();
					messageEntity.getRecipients().add(system);
				}
			}
			messageEntity = this.messageService.reconstruct(messageEntity, binding);
			if (binding.hasErrors()) {
				result = this.createEditModelAndView(messageEntity);
				if (broadcast != null)
					result.addObject("broadcast", broadcast);
			} else {
				if (broadcast != null)
					this.messageService.save(messageEntity, true);
				else
					this.messageService.save(messageEntity, false);
				result = new ModelAndView("redirect:/message/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(messageEntity, "hacking.logged.error");
			else
				result = this.createEditModelAndView(messageEntity, "commit.error");

		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Message messageEntity) {
		ModelAndView res;
		res = this.createEditModelAndView(messageEntity, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final Message messageEntity, final String message) {
		ModelAndView result;

		if (messageEntity == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			result = new ModelAndView("message/create");
			result.addObject("messageEntity", messageEntity);
			final Collection<Actor> recipients = this.actorService.findAllActorsExceptLogged();
			result.addObject("recipients", recipients);
		}

		result.addObject("message", message);

		return result;
	}
}
