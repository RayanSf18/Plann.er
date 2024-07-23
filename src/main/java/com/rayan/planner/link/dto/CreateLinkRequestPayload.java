package com.rayan.planner.link.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for creating a link")
public record CreateLinkRequestPayload(

          @Schema(description = "Title of the link", example = "Check the tickets in the CVC")
          @NotBlank String title,

          @Schema(description = "Url of the link", example = "https://www.cvc.com.br/p/passagens-aereas")
          @NotBlank String url

) {
}
